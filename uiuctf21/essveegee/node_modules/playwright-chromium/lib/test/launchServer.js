"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.LaunchServers = void 0;

var _net = _interopRequireDefault(require("net"));

var _os = _interopRequireDefault(require("os"));

var _stream = _interopRequireDefault(require("stream"));

var _util = require("./util");

var _processLauncher = require("../utils/processLauncher");

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

/* eslint-disable no-console */
const DEFAULT_ENVIRONMENT_VARIABLES = {
  'BROWSER': 'none' // Disable that create-react-app will open the page in the browser

};

const newProcessLogPrefixer = () => new _stream.default.Transform({
  transform(chunk, encoding, callback) {
    this.push(chunk.toString().split(_os.default.EOL).map(line => line ? `[Launch] ${line}` : line).join(_os.default.EOL));
    callback();
  }

});

class LaunchServer {
  constructor(config) {
    this.config = config;
    this._killProcess = void 0;
    this._processExitedPromise = void 0;
  }

  static async create(config) {
    const launchServer = new LaunchServer(config);

    try {
      await launchServer._startProcess();
      await launchServer._waitForProcess();
      return launchServer;
    } catch (error) {
      await launchServer.kill();
      throw error;
    }
  }

  async _startProcess() {
    let processExitedReject = error => {};

    this._processExitedPromise = new Promise((_, reject) => processExitedReject = reject);

    if (this.config.waitForPort) {
      const portIsUsed = !(await canBindPort(this.config.waitForPort));
      if (portIsUsed && this.config.strict) throw new Error(`Port ${this.config.waitForPort} is used, make sure that nothing is running on the port or set strict:false in config.launch.`);
      if (portIsUsed) return;
    }

    console.log(`Launching '${this.config.command}'...`);
    const {
      launchedProcess,
      kill
    } = await (0, _processLauncher.launchProcess)({
      command: this.config.command,
      env: { ...DEFAULT_ENVIRONMENT_VARIABLES,
        ...process.env,
        ...this.config.env
      },
      cwd: this.config.cwd,
      stdio: 'stdin',
      shell: true,
      attemptToGracefullyClose: async () => {},
      log: () => {},
      onExit: code => processExitedReject(new Error(`Process from config.launch was not able to start. Exit code: ${code}`)),
      tempDirectories: []
    });
    this._killProcess = kill;
    launchedProcess.stderr.pipe(newProcessLogPrefixer()).pipe(process.stderr);
    launchedProcess.stdout.on('data', () => {});
  }

  async _waitForProcess() {
    if (this.config.waitForPort) {
      await this._waitForAvailability(this.config.waitForPort);
      const baseURL = `http://localhost:${this.config.waitForPort}`;
      process.env.PLAYWRIGHT_TEST_BASE_URL = baseURL;
      console.log(`Using baseURL '${baseURL}' from config.launch.`);
    }
  }

  async _waitForAvailability(port) {
    const launchTimeout = this.config.waitForPortTimeout || 60 * 1000;
    const cancellationToken = {
      canceled: false
    };
    const {
      timedOut
    } = await Promise.race([(0, _util.raceAgainstDeadline)(waitForSocket(port, 100, cancellationToken), launchTimeout + (0, _util.monotonicTime)()), this._processExitedPromise]);
    cancellationToken.canceled = true;
    if (timedOut) throw new Error(`Timed out waiting ${launchTimeout}ms from config.launch.`);
  }

  async kill() {
    var _this$_killProcess;

    await ((_this$_killProcess = this._killProcess) === null || _this$_killProcess === void 0 ? void 0 : _this$_killProcess.call(this));
  }

}

async function canBindPort(port) {
  return new Promise(resolve => {
    const server = _net.default.createServer();

    server.on('error', () => resolve(false));
    server.listen(port, () => {
      server.close(() => {
        resolve(true);
      });
    });
  });
}

async function waitForSocket(port, delay, cancellationToken) {
  while (!cancellationToken.canceled) {
    const connected = await new Promise(resolve => {
      const conn = _net.default.connect(port).on('error', () => {
        resolve(false);
      }).on('connect', () => {
        conn.end();
        resolve(true);
      });
    });
    if (connected) return;
    await new Promise(x => setTimeout(x, delay));
  }
}

class LaunchServers {
  constructor() {
    this._servers = [];
  }

  static async create(configs) {
    const launchServers = new LaunchServers();

    try {
      for (const config of configs) launchServers._servers.push(await LaunchServer.create(config));
    } catch (error) {
      for (const server of launchServers._servers) await server.kill();

      throw error;
    }

    return launchServers;
  }

  async killAll() {
    for (const server of this._servers) await server.kill();
  }

}

exports.LaunchServers = LaunchServers;
//# sourceMappingURL=launchServer.js.map