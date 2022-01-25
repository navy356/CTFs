"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.Dispatcher = void 0;

var _child_process = _interopRequireDefault(require("child_process"));

var _path = _interopRequireDefault(require("path"));

var _events = require("events");

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

/**
 * Copyright Microsoft Corporation. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
class Dispatcher {
  constructor(loader, suite, reporter) {
    this._workers = new Set();
    this._freeWorkers = [];
    this._workerClaimers = [];
    this._testById = new Map();
    this._queue = [];

    this._stopCallback = () => {};

    this._loader = void 0;
    this._suite = void 0;
    this._reporter = void 0;
    this._hasWorkerErrors = false;
    this._isStopped = false;
    this._failureCount = 0;
    this._loader = loader;
    this._reporter = reporter;
    this._suite = suite;

    for (const suite of this._suite.suites) {
      for (const test of suite.allTests()) this._testById.set(test._id, {
        test,
        result: test._appendTestResult()
      });
    }

    this._queue = this._filesSortedByWorkerHash(); // Shard tests.

    const shard = this._loader.fullConfig().shard;

    if (shard) {
      let total = this._suite.allTests().length;

      const shardSize = Math.ceil(total / shard.total);
      const from = shardSize * (shard.current - 1);
      const to = shardSize * shard.current;
      let current = 0;
      total = 0;
      const filteredQueue = [];

      for (const entry of this._queue) {
        if (current >= from && current < to) {
          filteredQueue.push(entry);
          total += entry.runPayload.entries.length;
        }

        current += entry.runPayload.entries.length;
      }

      this._queue = filteredQueue;
    }
  }

  _filesSortedByWorkerHash() {
    const entriesByWorkerHashAndFile = new Map();

    for (const projectSuite of this._suite.suites) {
      for (const test of projectSuite.allTests()) {
        let entriesByFile = entriesByWorkerHashAndFile.get(test._workerHash);

        if (!entriesByFile) {
          entriesByFile = new Map();
          entriesByWorkerHashAndFile.set(test._workerHash, entriesByFile);
        }

        const file = test._requireFile;
        let entry = entriesByFile.get(file);

        if (!entry) {
          entry = {
            runPayload: {
              entries: [],
              file
            },
            repeatEachIndex: test._repeatEachIndex,
            projectIndex: test._projectIndex,
            hash: test._workerHash
          };
          entriesByFile.set(file, entry);
        }

        entry.runPayload.entries.push({
          retry: this._testById.get(test._id).result.retry,
          testId: test._id
        });
      }
    }

    const result = [];

    for (const entriesByFile of entriesByWorkerHashAndFile.values()) {
      for (const entry of entriesByFile.values()) result.push(entry);
    }

    result.sort((a, b) => a.hash < b.hash ? -1 : a.hash === b.hash ? 0 : 1);
    return result;
  }

  async run() {
    // Loop in case job schedules more jobs
    while (this._queue.length && !this._isStopped) await this._dispatchQueue();
  }

  async _dispatchQueue() {
    const jobs = [];

    while (this._queue.length) {
      if (this._isStopped) break;

      const entry = this._queue.shift();

      const requiredHash = entry.hash;
      let worker = await this._obtainWorker(entry);

      while (!this._isStopped && worker.hash && worker.hash !== requiredHash) {
        worker.stop();
        worker = await this._obtainWorker(entry);
      }

      if (this._isStopped) break;
      jobs.push(this._runJob(worker, entry));
    }

    await Promise.all(jobs);
  }

  async _runJob(worker, entry) {
    worker.run(entry.runPayload);

    let doneCallback = () => {};

    const result = new Promise(f => doneCallback = f);

    const doneWithJob = () => {
      worker.removeListener('testBegin', onTestBegin);
      worker.removeListener('testEnd', onTestEnd);
      worker.removeListener('done', onDone);
      worker.removeListener('exit', onExit);
      doneCallback();
    };

    const remainingByTestId = new Map(entry.runPayload.entries.map(e => [e.testId, e]));
    let lastStartedTestId;

    const onTestBegin = params => {
      lastStartedTestId = params.testId;
    };

    worker.addListener('testBegin', onTestBegin);

    const onTestEnd = params => {
      remainingByTestId.delete(params.testId);
    };

    worker.addListener('testEnd', onTestEnd);

    const onDone = params => {
      let remaining = [...remainingByTestId.values()]; // We won't file remaining if:
      // - there are no remaining
      // - we are here not because something failed
      // - no unrecoverable worker error

      if (!remaining.length && !params.failedTestId && !params.fatalError) {
        this._freeWorkers.push(worker);

        this._notifyWorkerClaimer();

        doneWithJob();
        return;
      } // When worker encounters error, we will stop it and create a new one.


      worker.stop();
      const failedTestIds = new Set(); // In case of fatal error, report all remaining tests as failing with this error.

      if (params.fatalError) {
        for (const {
          testId
        } of remaining) {
          const {
            test,
            result
          } = this._testById.get(testId); // There might be a single test that has started but has not finished yet.


          if (testId !== lastStartedTestId) this._reportTestBegin(test);
          result.error = params.fatalError;

          this._reportTestEnd(test, result, 'failed');

          failedTestIds.add(testId);
        } // Since we pretend that all remaining tests failed, there is nothing else to run,
        // except for possible retries.


        remaining = [];
      }

      if (params.failedTestId) failedTestIds.add(params.failedTestId); // Only retry expected failures, not passes and only if the test failed.

      for (const testId of failedTestIds) {
        const pair = this._testById.get(testId);

        if (!this._isStopped && pair.test.expectedStatus === 'passed' && pair.test.results.length < pair.test.retries + 1) {
          pair.result = pair.test._appendTestResult();
          remaining.unshift({
            retry: pair.result.retry,
            testId: pair.test._id
          });
        }
      }

      if (remaining.length) this._queue.unshift({ ...entry,
        runPayload: { ...entry.runPayload,
          entries: remaining
        }
      }); // This job is over, we just scheduled another one.

      doneWithJob();
    };

    worker.on('done', onDone);

    const onExit = () => {
      onDone({
        fatalError: {
          value: 'Worker process exited unexpectedly'
        }
      });
    };

    worker.on('exit', onExit);
    return result;
  }

  async _obtainWorker(entry) {
    const claimWorker = () => {
      // Use available worker.
      if (this._freeWorkers.length) return Promise.resolve(this._freeWorkers.pop()); // Create a new worker.

      if (this._workers.size < this._loader.fullConfig().workers) return this._createWorker(entry);
      return null;
    }; // Note: it is important to claim the worker synchronously,
    // so that we won't miss a _notifyWorkerClaimer call while awaiting.


    let worker = claimWorker();

    if (!worker) {
      // Wait for available or stopped worker.
      await new Promise(f => this._workerClaimers.push(f));
      worker = claimWorker();
    }

    return worker;
  }

  async _notifyWorkerClaimer() {
    if (this._isStopped || !this._workerClaimers.length) return;

    const callback = this._workerClaimers.shift();

    callback();
  }

  _createWorker(entry) {
    const worker = new Worker(this);
    worker.on('testBegin', params => {
      const {
        test,
        result: testRun
      } = this._testById.get(params.testId);

      testRun.workerIndex = params.workerIndex;
      testRun.startTime = new Date(params.startWallTime);

      this._reportTestBegin(test);
    });
    worker.on('testEnd', params => {
      const {
        test,
        result
      } = this._testById.get(params.testId);

      result.duration = params.duration;
      result.error = params.error;
      result.attachments = params.attachments.map(a => ({
        name: a.name,
        path: a.path,
        contentType: a.contentType,
        body: a.body ? Buffer.from(a.body, 'base64') : undefined
      }));
      test.expectedStatus = params.expectedStatus;
      test.annotations = params.annotations;
      test.timeout = params.timeout;

      this._reportTestEnd(test, result, params.status);
    });
    worker.on('stdOut', params => {
      var _this$_reporter$onStd, _this$_reporter;

      const chunk = chunkFromParams(params);
      const pair = params.testId ? this._testById.get(params.testId) : undefined;
      if (pair) pair.result.stdout.push(chunk);
      (_this$_reporter$onStd = (_this$_reporter = this._reporter).onStdOut) === null || _this$_reporter$onStd === void 0 ? void 0 : _this$_reporter$onStd.call(_this$_reporter, chunk, pair ? pair.test : undefined);
    });
    worker.on('stdErr', params => {
      var _this$_reporter$onStd2, _this$_reporter2;

      const chunk = chunkFromParams(params);
      const pair = params.testId ? this._testById.get(params.testId) : undefined;
      if (pair) pair.result.stderr.push(chunk);
      (_this$_reporter$onStd2 = (_this$_reporter2 = this._reporter).onStdErr) === null || _this$_reporter$onStd2 === void 0 ? void 0 : _this$_reporter$onStd2.call(_this$_reporter2, chunk, pair ? pair.test : undefined);
    });
    worker.on('teardownError', ({
      error
    }) => {
      var _this$_reporter$onErr, _this$_reporter3;

      this._hasWorkerErrors = true;
      (_this$_reporter$onErr = (_this$_reporter3 = this._reporter).onError) === null || _this$_reporter$onErr === void 0 ? void 0 : _this$_reporter$onErr.call(_this$_reporter3, error);
    });
    worker.on('exit', () => {
      this._workers.delete(worker);

      this._notifyWorkerClaimer();

      if (this._stopCallback && !this._workers.size) this._stopCallback();
    });

    this._workers.add(worker);

    return worker.init(entry).then(() => worker);
  }

  async stop() {
    this._isStopped = true;

    if (this._workers.size) {
      const result = new Promise(f => this._stopCallback = f);

      for (const worker of this._workers) worker.stop();

      await result;
    }
  }

  _reportTestBegin(test) {
    var _this$_reporter$onTes, _this$_reporter4;

    if (this._isStopped) return;

    const maxFailures = this._loader.fullConfig().maxFailures;

    if (!maxFailures || this._failureCount < maxFailures) (_this$_reporter$onTes = (_this$_reporter4 = this._reporter).onTestBegin) === null || _this$_reporter$onTes === void 0 ? void 0 : _this$_reporter$onTes.call(_this$_reporter4, test);
  }

  _reportTestEnd(test, result, status) {
    var _this$_reporter$onTes2, _this$_reporter5;

    if (this._isStopped) return;
    result.status = status;
    if (result.status !== 'skipped' && result.status !== test.expectedStatus) ++this._failureCount;

    const maxFailures = this._loader.fullConfig().maxFailures;

    if (!maxFailures || this._failureCount <= maxFailures) (_this$_reporter$onTes2 = (_this$_reporter5 = this._reporter).onTestEnd) === null || _this$_reporter$onTes2 === void 0 ? void 0 : _this$_reporter$onTes2.call(_this$_reporter5, test, result);
    if (maxFailures && this._failureCount === maxFailures) this.stop().catch(e => {});
  }

  hasWorkerErrors() {
    return this._hasWorkerErrors;
  }

}

exports.Dispatcher = Dispatcher;
let lastWorkerIndex = 0;

class Worker extends _events.EventEmitter {
  constructor(runner) {
    super();
    this.process = void 0;
    this.runner = void 0;
    this.hash = '';
    this.index = void 0;
    this.didSendStop = false;
    this.runner = runner;
    this.index = lastWorkerIndex++;
    this.process = _child_process.default.fork(_path.default.join(__dirname, 'worker.js'), {
      detached: false,
      env: {
        FORCE_COLOR: process.stdout.isTTY ? '1' : '0',
        DEBUG_COLORS: process.stdout.isTTY ? '1' : '0',
        TEST_WORKER_INDEX: String(this.index),
        ...process.env
      },
      // Can't pipe since piping slows down termination for some reason.
      stdio: ['ignore', 'ignore', process.env.PW_RUNNER_DEBUG ? 'inherit' : 'ignore', 'ipc']
    });
    this.process.on('exit', () => this.emit('exit'));
    this.process.on('error', e => {}); // do not yell at a send to dead process.

    this.process.on('message', message => {
      const {
        method,
        params
      } = message;
      this.emit(method, params);
    });
  }

  async init(entry) {
    this.hash = entry.hash;
    const params = {
      workerIndex: this.index,
      repeatEachIndex: entry.repeatEachIndex,
      projectIndex: entry.projectIndex,
      loader: this.runner._loader.serialize()
    };
    this.process.send({
      method: 'init',
      params
    });
    await new Promise(f => this.process.once('message', f)); // Ready ack
  }

  run(runPayload) {
    this.process.send({
      method: 'run',
      params: runPayload
    });
  }

  stop() {
    if (!this.didSendStop) this.process.send({
      method: 'stop'
    });
    this.didSendStop = true;
  }

}

function chunkFromParams(params) {
  if (typeof params.text === 'string') return params.text;
  return Buffer.from(params.buffer, 'base64');
}
//# sourceMappingURL=dispatcher.js.map