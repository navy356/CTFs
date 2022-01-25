"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.TraceSnapshotter = void 0;

var _events = require("events");

var _fs = _interopRequireDefault(require("fs"));

var _path = _interopRequireDefault(require("path"));

var _snapshotter = require("../../snapshot/snapshotter");

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
class TraceSnapshotter extends _events.EventEmitter {
  constructor(context, resourcesDir, appendTraceEvent) {
    super();
    this._snapshotter = void 0;
    this._resourcesDir = void 0;
    this._writeArtifactChain = Promise.resolve();
    this._appendTraceEvent = void 0;
    this._resourcesDir = resourcesDir;
    this._snapshotter = new _snapshotter.Snapshotter(context, this);
    this._appendTraceEvent = appendTraceEvent;
    this._writeArtifactChain = Promise.resolve();
  }

  started() {
    return this._snapshotter.started();
  }

  async start() {
    await this._snapshotter.start();
  }

  async stop() {
    await this._snapshotter.stop();
    await this._writeArtifactChain;
  }

  async dispose() {
    this._snapshotter.dispose();

    await this._writeArtifactChain;
  }

  async captureSnapshot(page, snapshotName, element) {
    await this._snapshotter.captureSnapshot(page, snapshotName, element).catch(() => {});
  }

  onBlob(blob) {
    this._writeArtifactChain = this._writeArtifactChain.then(async () => {
      await _fs.default.promises.writeFile(_path.default.join(this._resourcesDir, blob.sha1), blob.buffer).catch(() => {});
    });
  }

  onResourceSnapshot(snapshot) {
    this._appendTraceEvent({
      type: 'resource-snapshot',
      snapshot
    });
  }

  onFrameSnapshot(snapshot) {
    this._appendTraceEvent({
      type: 'frame-snapshot',
      snapshot
    });
  }

}

exports.TraceSnapshotter = TraceSnapshotter;
//# sourceMappingURL=traceSnapshotter.js.map