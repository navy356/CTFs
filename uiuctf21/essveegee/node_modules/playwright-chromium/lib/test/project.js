"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.ProjectImpl = void 0;

var _test = require("./test");

var _fixtures = require("./fixtures");

var _testType = require("./testType");

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
class ProjectImpl {
  constructor(project, index) {
    this.config = void 0;
    this.index = void 0;
    this.defines = new Map();
    this.testTypePools = new Map();
    this.testPools = new Map();
    this.config = project;
    this.index = index;
    this.defines = new Map();

    for (const {
      test,
      fixtures
    } of Array.isArray(project.define) ? project.define : [project.define]) this.defines.set(test, fixtures);
  }

  buildTestTypePool(testType) {
    if (!this.testTypePools.has(testType)) {
      const fixtures = this.resolveFixtures(testType);
      const overrides = this.config.use;
      const overridesWithLocation = {
        fixtures: overrides,
        location: {
          file: `<configuration file>`,
          line: 1,
          column: 1
        }
      };
      const pool = new _fixtures.FixturePool([...fixtures, overridesWithLocation]);
      this.testTypePools.set(testType, pool);
    }

    return this.testTypePools.get(testType);
  } // TODO: we can optimize this function by building the pool inline in cloneSuite


  buildPool(test) {
    if (!this.testPools.has(test)) {
      let pool = this.buildTestTypePool(test._testType);

      const overrides = test.parent._buildFixtureOverrides();

      if (Object.entries(overrides).length) {
        const overridesWithLocation = {
          fixtures: overrides,
          // TODO: pass location from test.use() callsite.
          location: test.location
        };
        pool = new _fixtures.FixturePool([overridesWithLocation], pool);
      }

      this.testPools.set(test, pool);
      pool.validateFunction(test.fn, 'Test', true, test.location);

      for (let parent = test.parent; parent; parent = parent.parent) {
        for (const hook of parent._hooks) pool.validateFunction(hook.fn, hook.type + ' hook', hook.type === 'beforeEach' || hook.type === 'afterEach', hook.location);

        for (const modifier of parent._modifiers) pool.validateFunction(modifier.fn, modifier.type + ' modifier', true, modifier.location);
      }
    }

    return this.testPools.get(test);
  }

  _cloneEntries(from, to, repeatEachIndex, filter) {
    for (const entry of from._entries) {
      if (entry instanceof _test.Suite) {
        const suite = entry._clone();

        to._addSuite(suite);

        if (!this._cloneEntries(entry, suite, repeatEachIndex, filter)) {
          to._entries.pop();

          to.suites.pop();
        }
      } else {
        const pool = this.buildPool(entry);

        const test = entry._clone();

        test.projectName = this.config.name;
        test.retries = this.config.retries;
        test._workerHash = `run${this.index}-${pool.digest}-repeat${repeatEachIndex}`;
        test._id = `${entry._ordinalInFile}@${entry._requireFile}#run${this.index}-repeat${repeatEachIndex}`;
        test._pool = pool;
        test._repeatEachIndex = repeatEachIndex;
        test._projectIndex = this.index;

        to._addTest(test);

        if (!filter(test)) {
          to._entries.pop();

          to.suites.pop();
        }
      }
    }

    return to._entries.length > 0;
  }

  cloneFileSuite(suite, repeatEachIndex, filter) {
    const result = suite._clone();

    return this._cloneEntries(suite, result, repeatEachIndex, filter) ? result : undefined;
  }

  resolveFixtures(testType) {
    return testType.fixtures.map(f => {
      if (f instanceof _testType.DeclaredFixtures) {
        const fixtures = this.defines.get(f.testType.test) || {};
        return {
          fixtures,
          location: f.location
        };
      }

      return f;
    });
  }

}

exports.ProjectImpl = ProjectImpl;
//# sourceMappingURL=project.js.map