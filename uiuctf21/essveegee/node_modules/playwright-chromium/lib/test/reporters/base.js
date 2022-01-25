"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.formatFailure = formatFailure;
exports.formatTestTitle = formatTestTitle;
exports.stripAscii = stripAscii;
exports.BaseReporter = void 0;

var _codeFrame = require("@babel/code-frame");

var _safe = _interopRequireDefault(require("colors/safe"));

var _fs = _interopRequireDefault(require("fs"));

var _ms = _interopRequireDefault(require("ms"));

var _path = _interopRequireDefault(require("path"));

var _stackUtils = _interopRequireDefault(require("stack-utils"));

function _interopRequireDefault(obj) { return obj && obj.__esModule ? obj : { default: obj }; }

/**
 * Copyright (c) Microsoft Corporation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
// @ts-ignore
const stackUtils = new _stackUtils.default();

class BaseReporter {
  constructor() {
    this.duration = 0;
    this.config = void 0;
    this.suite = void 0;
    this.result = void 0;
    this.fileDurations = new Map();
    this.monotonicStartTime = 0;
  }

  onBegin(config, suite) {
    this.monotonicStartTime = monotonicTime();
    this.config = config;
    this.suite = suite;
  }

  onStdOut(chunk) {
    if (!this.config.quiet) process.stdout.write(chunk);
  }

  onStdErr(chunk) {
    if (!this.config.quiet) process.stderr.write(chunk);
  }

  onTestEnd(test, result) {
    const projectName = test.titlePath()[1];
    const relativePath = relativeTestPath(this.config, test);
    const fileAndProject = (projectName ? `[${projectName}] › ` : '') + relativePath;
    const duration = this.fileDurations.get(fileAndProject) || 0;
    this.fileDurations.set(fileAndProject, duration + result.duration);
  }

  onError(error) {
    console.log(formatError(error));
  }

  async onEnd(result) {
    this.duration = monotonicTime() - this.monotonicStartTime;
    this.result = result;
  }

  _printSlowTests() {
    if (!this.config.reportSlowTests) return;
    const fileDurations = [...this.fileDurations.entries()];
    fileDurations.sort((a, b) => b[1] - a[1]);
    const count = Math.min(fileDurations.length, this.config.reportSlowTests.max || Number.POSITIVE_INFINITY);

    for (let i = 0; i < count; ++i) {
      const duration = fileDurations[i][1];
      if (duration <= this.config.reportSlowTests.threshold) break;
      console.log(_safe.default.yellow('  Slow test: ') + fileDurations[i][0] + _safe.default.yellow(` (${(0, _ms.default)(duration)})`));
    }
  }

  epilogue(full) {
    let skipped = 0;
    let expected = 0;
    const unexpected = [];
    const flaky = [];
    this.suite.allTests().forEach(test => {
      switch (test.outcome()) {
        case 'skipped':
          ++skipped;
          break;

        case 'expected':
          ++expected;
          break;

        case 'unexpected':
          unexpected.push(test);
          break;

        case 'flaky':
          flaky.push(test);
          break;
      }
    });

    if (full && unexpected.length) {
      console.log('');

      this._printFailures(unexpected);
    }

    this._printSlowTests();

    console.log('');

    if (unexpected.length) {
      console.log(_safe.default.red(`  ${unexpected.length} failed`));

      this._printTestHeaders(unexpected);
    }

    if (flaky.length) {
      console.log(_safe.default.red(`  ${flaky.length} flaky`));

      this._printTestHeaders(flaky);
    }

    if (skipped) console.log(_safe.default.yellow(`  ${skipped} skipped`));
    if (expected) console.log(_safe.default.green(`  ${expected} passed`) + _safe.default.dim(` (${(0, _ms.default)(this.duration)})`));
    if (this.result.status === 'timedout') console.log(_safe.default.red(`  Timed out waiting ${this.config.globalTimeout / 1000}s for the entire test run`));
  }

  _printTestHeaders(tests) {
    tests.forEach(test => {
      console.log(formatTestHeader(this.config, test, '    '));
    });
  }

  _printFailures(failures) {
    failures.forEach((test, index) => {
      console.log(formatFailure(this.config, test, index + 1));
    });
  }

  hasResultWithStatus(test, status) {
    return !!test.results.find(r => r.status === status);
  }

  willRetry(test, result) {
    return result.status !== 'passed' && result.status !== test.expectedStatus && test.results.length <= test.retries;
  }

}

exports.BaseReporter = BaseReporter;

function formatFailure(config, test, index) {
  const tokens = [];
  tokens.push(formatTestHeader(config, test, '  ', index));

  for (const result of test.results) {
    if (result.status === 'passed') continue;
    tokens.push(formatFailedResult(test, result));
  }

  tokens.push('');
  return tokens.join('\n');
}

function relativeTestPath(config, test) {
  return _path.default.relative(config.rootDir, test.location.file) || _path.default.basename(test.location.file);
}

function formatTestTitle(config, test) {
  // root, project, file, ...describes, test
  const [, projectName,, ...titles] = test.titlePath();
  const location = `${relativeTestPath(config, test)}:${test.location.line}:${test.location.column}`;
  const projectTitle = projectName ? `[${projectName}] › ` : '';
  return `${projectTitle}${location} › ${titles.join(' ')}`;
}

function formatTestHeader(config, test, indent, index) {
  const title = formatTestTitle(config, test);
  const passedUnexpectedlySuffix = test.results[0].status === 'passed' ? ' -- passed unexpectedly' : '';
  const header = `${indent}${index ? index + ') ' : ''}${title}${passedUnexpectedlySuffix}`;
  return _safe.default.red(pad(header, '='));
}

function formatFailedResult(test, result) {
  const tokens = [];
  if (result.retry) tokens.push(_safe.default.gray(pad(`\n    Retry #${result.retry}`, '-')));

  if (result.status === 'timedOut') {
    tokens.push('');
    tokens.push(indent(_safe.default.red(`Timeout of ${test.timeout}ms exceeded.`), '    '));
    if (result.error !== undefined) tokens.push(indent(formatError(result.error, test.location.file), '    '));
  } else {
    tokens.push(indent(formatError(result.error, test.location.file), '    '));
  }

  return tokens.join('\n');
}

function formatError(error, file) {
  const stack = error.stack;
  const tokens = [];

  if (stack) {
    tokens.push('');
    const message = error.message || '';
    const messageLocation = stack.indexOf(message);
    const preamble = stack.substring(0, messageLocation + message.length);
    tokens.push(preamble);
    const position = file ? positionInFile(stack, file) : null;

    if (position) {
      const source = _fs.default.readFileSync(file, 'utf8');

      tokens.push('');
      tokens.push((0, _codeFrame.codeFrameColumns)(source, {
        start: position
      }, {
        highlightCode: _safe.default.enabled
      }));
    }

    tokens.push('');
    tokens.push(_safe.default.dim(preamble.length > 0 ? stack.substring(preamble.length + 1) : stack));
  } else {
    tokens.push('');
    tokens.push(error.value);
  }

  return tokens.join('\n');
}

function pad(line, char) {
  return line + ' ' + _safe.default.gray(char.repeat(Math.max(0, 100 - line.length - 1)));
}

function indent(lines, tab) {
  return lines.replace(/^(?=.+$)/gm, tab);
}

function positionInFile(stack, file) {
  // Stack will have /private/var/folders instead of /var/folders on Mac.
  file = _fs.default.realpathSync(file);

  for (const line of stack.split('\n')) {
    const parsed = stackUtils.parseLine(line);
    if (!parsed || !parsed.file) continue;
    if (_path.default.resolve(process.cwd(), parsed.file) === file) return {
      column: parsed.column || 0,
      line: parsed.line || 0
    };
  }
}

function monotonicTime() {
  const [seconds, nanoseconds] = process.hrtime();
  return seconds * 1000 + (nanoseconds / 1000000 | 0);
}

const asciiRegex = new RegExp('[\\u001B\\u009B][[\\]()#;?]*(?:(?:(?:[a-zA-Z\\d]*(?:;[-a-zA-Z\\d\\/#&.:=?%@~_]*)*)?\\u0007)|(?:(?:\\d{1,4}(?:;\\d{0,4})*)?[\\dA-PR-TZcf-ntqry=><~]))', 'g');

function stripAscii(str) {
  return str.replace(asciiRegex, '');
}
//# sourceMappingURL=base.js.map