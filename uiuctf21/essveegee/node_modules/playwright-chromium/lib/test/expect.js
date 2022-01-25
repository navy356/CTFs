"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.expect = void 0;

var _expect = _interopRequireDefault(require("expect"));

var _globals = require("./globals");

var _golden = require("./golden");

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
const expect = _expect.default;
exports.expect = expect;

function toMatchSnapshot(received, nameOrOptions, optOptions = {}) {
  var _testInfo$project$exp, _testInfo$project$exp2;

  let options;
  const testInfo = (0, _globals.currentTestInfo)();
  if (!testInfo) throw new Error(`toMatchSnapshot() must be called during the test`);
  if (typeof nameOrOptions === 'string') options = {
    name: nameOrOptions,
    ...optOptions
  };else options = { ...nameOrOptions
  };
  if (!options.name) throw new Error(`toMatchSnapshot() requires a "name" parameter`);
  const projectThreshold = (_testInfo$project$exp = testInfo.project.expect) === null || _testInfo$project$exp === void 0 ? void 0 : (_testInfo$project$exp2 = _testInfo$project$exp.toMatchSnapshot) === null || _testInfo$project$exp2 === void 0 ? void 0 : _testInfo$project$exp2.threshold;
  if (options.threshold === undefined && projectThreshold !== undefined) options.threshold = projectThreshold;
  const withNegateComparison = this.isNot;
  const {
    pass,
    message,
    expectedPath,
    actualPath,
    diffPath,
    mimeType
  } = (0, _golden.compare)(received, options.name, testInfo.snapshotPath, testInfo.outputPath, testInfo.config.updateSnapshots, withNegateComparison, options);
  const contentType = mimeType || 'application/octet-stream';
  if (expectedPath) testInfo.attachments.push({
    name: 'expected',
    contentType,
    path: expectedPath
  });
  if (actualPath) testInfo.attachments.push({
    name: 'actual',
    contentType,
    path: actualPath
  });
  if (diffPath) testInfo.attachments.push({
    name: 'diff',
    contentType,
    path: diffPath
  });
  return {
    pass,
    message: () => message
  };
}

_expect.default.extend({
  toMatchSnapshot
});

_expect.default.setState({
  expand: false
});
//# sourceMappingURL=expect.js.map