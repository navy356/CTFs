"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.shouldCaptureSnapshot = shouldCaptureSnapshot;
exports.Tracing = exports.VERSION = void 0;

var _fs = _interopRequireDefault(require("fs"));

var _path = _interopRequireDefault(require("path"));

var _yazl = _interopRequireDefault(require("yazl"));

var _utils = require("../../../utils/utils");

var _artifact = require("../../artifact");

var _browserContext = require("../../browserContext");

var _eventsHelper = require("../../../utils/eventsHelper");

var _page = require("../../page");

var _traceSnapshotter = require("./traceSnapshotter");

var _channels = require("../../../protocol/channels");

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

/**
 * Copyright (c) Microsoft Corporation.
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
const VERSION = 1;
exports.VERSION = VERSION;

class Tracing {
  constructor(context) {
    this._appendEventChain = Promise.resolve();
    this._snapshotter = void 0;
    this._eventListeners = [];
    this._pendingCalls = new Map();
    this._context = void 0;
    this._traceFile = void 0;
    this._resourcesDir = void 0;
    this._sha1s = [];
    this._recordingTraceEvents = false;
    this._tracesDir = void 0;
    this._context = context;
    this._tracesDir = context._browser.options.tracesDir;
    this._resourcesDir = _path.default.join(this._tracesDir, 'resources');
    this._snapshotter = new _traceSnapshotter.TraceSnapshotter(this._context, this._resourcesDir, traceEvent => this._appendTraceEvent(traceEvent));
  }

  async start(options) {
    // context + page must be the first events added, this method can't have awaits before them.
    if (this._recordingTraceEvents) throw new Error('Tracing has already been started');
    this._recordingTraceEvents = true;
    this._traceFile = _path.default.join(this._tracesDir, (options.name || (0, _utils.createGuid)()) + '.trace');
    this._appendEventChain = (0, _utils.mkdirIfNeeded)(this._traceFile);
    const event = {
      version: VERSION,
      type: 'context-options',
      browserName: this._context._browser.options.name,
      options: this._context._options
    };

    this._appendTraceEvent(event);

    for (const page of this._context.pages()) this._onPage(options.screenshots, page);

    this._eventListeners.push(_eventsHelper.eventsHelper.addEventListener(this._context, _browserContext.BrowserContext.Events.Page, this._onPage.bind(this, options.screenshots))); // context + page must be the first events added, no awaits above this line.


    await _fs.default.promises.mkdir(this._resourcesDir, {
      recursive: true
    });

    this._context.instrumentation.addListener(this);

    if (options.snapshots) await this._snapshotter.start();
  }

  async stop() {
    if (!this._eventListeners.length) return;

    this._context.instrumentation.removeListener(this);

    _eventsHelper.eventsHelper.removeEventListeners(this._eventListeners);

    for (const {
      sdkObject,
      metadata,
      beforeSnapshot,
      actionSnapshot,
      afterSnapshot
    } of this._pendingCalls.values()) {
      await Promise.all([beforeSnapshot, actionSnapshot, afterSnapshot]);
      if (!afterSnapshot) metadata.error = {
        error: {
          name: 'Error',
          message: 'Action was interrupted'
        }
      };
      await this.onAfterCall(sdkObject, metadata);
    }

    for (const page of this._context.pages()) page.setScreencastOptions(null);

    await this._snapshotter.stop(); // Ensure all writes are finished.

    this._recordingTraceEvents = false;
    await this._appendEventChain;
  }

  async dispose() {
    await this._snapshotter.dispose();
  }

  async export() {
    if (!this._traceFile || this._recordingTraceEvents) throw new Error('Must start and stop tracing before exporting');
    const zipFile = new _yazl.default.ZipFile();
    const failedPromise = new Promise((_, reject) => zipFile.on('error', reject));
    const succeededPromise = new Promise(async fulfill => {
      zipFile.addFile(this._traceFile, 'trace.trace');
      const zipFileName = this._traceFile + '.zip';

      for (const sha1 of this._sha1s) zipFile.addFile(_path.default.join(this._resourcesDir, sha1), _path.default.join('resources', sha1));

      zipFile.end();
      await new Promise(f => {
        zipFile.outputStream.pipe(_fs.default.createWriteStream(zipFileName)).on('close', f);
      });
      const artifact = new _artifact.Artifact(this._context, zipFileName);
      artifact.reportFinished();
      fulfill(artifact);
    });
    return Promise.race([failedPromise, succeededPromise]);
  }

  async _captureSnapshot(name, sdkObject, metadata, element) {
    if (!sdkObject.attribution.page) return;
    if (!this._snapshotter.started()) return;
    if (!shouldCaptureSnapshot(metadata)) return;
    const snapshotName = `${name}@${metadata.id}`;
    metadata.snapshots.push({
      title: name,
      snapshotName
    });
    await this._snapshotter.captureSnapshot(sdkObject.attribution.page, snapshotName, element);
  }

  async onBeforeCall(sdkObject, metadata) {
    const beforeSnapshot = this._captureSnapshot('before', sdkObject, metadata);

    this._pendingCalls.set(metadata.id, {
      sdkObject,
      metadata,
      beforeSnapshot
    });

    await beforeSnapshot;
  }

  async onBeforeInputAction(sdkObject, metadata, element) {
    const actionSnapshot = this._captureSnapshot('action', sdkObject, metadata, element);

    this._pendingCalls.get(metadata.id).actionSnapshot = actionSnapshot;
    await actionSnapshot;
  }

  async onAfterCall(sdkObject, metadata) {
    const pendingCall = this._pendingCalls.get(metadata.id);

    if (!pendingCall || pendingCall.afterSnapshot) return;

    if (!sdkObject.attribution.page) {
      this._pendingCalls.delete(metadata.id);

      return;
    }

    pendingCall.afterSnapshot = this._captureSnapshot('after', sdkObject, metadata);
    await pendingCall.afterSnapshot;
    const event = {
      type: 'action',
      metadata,
      hasSnapshot: shouldCaptureSnapshot(metadata)
    };

    this._appendTraceEvent(event);

    this._pendingCalls.delete(metadata.id);
  }

  onEvent(sdkObject, metadata) {
    if (!sdkObject.attribution.page) return;
    const event = {
      type: 'event',
      metadata,
      hasSnapshot: false
    };

    this._appendTraceEvent(event);
  }

  _onPage(screenshots, page) {
    if (screenshots) page.setScreencastOptions({
      width: 800,
      height: 600,
      quality: 90
    });

    this._eventListeners.push(_eventsHelper.eventsHelper.addEventListener(page, _page.Page.Events.ScreencastFrame, params => {
      const sha1 = (0, _utils.calculateSha1)((0, _utils.createGuid)()); // no need to compute sha1 for screenshots

      const event = {
        type: 'screencast-frame',
        pageId: page.guid,
        sha1,
        width: params.width,
        height: params.height,
        timestamp: (0, _utils.monotonicTime)()
      };

      this._appendTraceEvent(event);

      this._appendEventChain = this._appendEventChain.then(async () => {
        await _fs.default.promises.writeFile(_path.default.join(this._resourcesDir, sha1), params.buffer).catch(() => {});
      });
    }));
  }

  _appendTraceEvent(event) {
    if (!this._recordingTraceEvents) return;

    const visit = object => {
      if (Array.isArray(object)) {
        object.forEach(visit);
        return;
      }

      if (typeof object === 'object') {
        for (const key in object) {
          if (key === 'sha1' || key.endsWith('Sha1')) {
            const sha1 = object[key];
            if (sha1) this._sha1s.push(sha1);
          }

          visit(object[key]);
        }

        return;
      }
    };

    visit(event); // Serialize all writes to the trace file.

    this._appendEventChain = this._appendEventChain.then(async () => {
      await _fs.default.promises.appendFile(this._traceFile, JSON.stringify(event) + '\n');
    });
  }

}

exports.Tracing = Tracing;

function shouldCaptureSnapshot(metadata) {
  return _channels.commandsWithTracingSnapshots.has(metadata.type + '.' + metadata.method);
}
//# sourceMappingURL=tracing.js.map