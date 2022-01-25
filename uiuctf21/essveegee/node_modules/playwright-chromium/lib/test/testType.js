"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.rootTestType = exports.TestTypeImpl = exports.DeclaredFixtures = void 0;

var _expect = require("./expect");

var _globals = require("./globals");

var _test = require("./test");

var _transform = require("./transform");

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
const countByFile = new Map();

class DeclaredFixtures {
  constructor() {
    this.testType = void 0;
    this.location = void 0;
  }

}

exports.DeclaredFixtures = DeclaredFixtures;

class TestTypeImpl {
  constructor(fixtures) {
    this.fixtures = void 0;
    this.test = void 0;
    this.fixtures = fixtures;
    const test = (0, _transform.wrapFunctionWithLocation)(this._createTest.bind(this, 'default'));
    test.expect = _expect.expect;
    test.only = (0, _transform.wrapFunctionWithLocation)(this._createTest.bind(this, 'only'));
    test.describe = (0, _transform.wrapFunctionWithLocation)(this._describe.bind(this, 'default'));
    test.describe.only = (0, _transform.wrapFunctionWithLocation)(this._describe.bind(this, 'only'));
    test.beforeEach = (0, _transform.wrapFunctionWithLocation)(this._hook.bind(this, 'beforeEach'));
    test.afterEach = (0, _transform.wrapFunctionWithLocation)(this._hook.bind(this, 'afterEach'));
    test.beforeAll = (0, _transform.wrapFunctionWithLocation)(this._hook.bind(this, 'beforeAll'));
    test.afterAll = (0, _transform.wrapFunctionWithLocation)(this._hook.bind(this, 'afterAll'));
    test.skip = (0, _transform.wrapFunctionWithLocation)(this._modifier.bind(this, 'skip'));
    test.fixme = (0, _transform.wrapFunctionWithLocation)(this._modifier.bind(this, 'fixme'));
    test.fail = (0, _transform.wrapFunctionWithLocation)(this._modifier.bind(this, 'fail'));
    test.slow = (0, _transform.wrapFunctionWithLocation)(this._modifier.bind(this, 'slow'));
    test.setTimeout = this._setTimeout.bind(this);
    test.use = (0, _transform.wrapFunctionWithLocation)(this._use.bind(this));
    test.extend = (0, _transform.wrapFunctionWithLocation)(this._extend.bind(this));
    test.declare = (0, _transform.wrapFunctionWithLocation)(this._declare.bind(this));
    this.test = test;
  }

  _createTest(type, location, title, fn) {
    const suite = (0, _globals.currentlyLoadingFileSuite)();
    if (!suite) throw new Error(`test() can only be called in a test file`);
    const ordinalInFile = countByFile.get(suite._requireFile) || 0;
    countByFile.set(suite._requireFile, ordinalInFile + 1);
    const test = new _test.TestCase(title, fn, ordinalInFile, this, location);
    test._requireFile = suite._requireFile;

    suite._addTest(test);

    if (type === 'only') test._only = true;
  }

  _describe(type, location, title, fn) {
    const suite = (0, _globals.currentlyLoadingFileSuite)();
    if (!suite) throw new Error(`describe() can only be called in a test file`);
    const child = new _test.Suite(title);
    child._requireFile = suite._requireFile;
    child.location = location;

    suite._addSuite(child);

    if (type === 'only') child._only = true;
    (0, _globals.setCurrentlyLoadingFileSuite)(child);
    fn();
    (0, _globals.setCurrentlyLoadingFileSuite)(suite);
  }

  _hook(name, location, fn) {
    const suite = (0, _globals.currentlyLoadingFileSuite)();
    if (!suite) throw new Error(`${name} hook can only be called in a test file`);

    suite._hooks.push({
      type: name,
      fn,
      location
    });
  }

  _modifier(type, location, ...modifierArgs) {
    const suite = (0, _globals.currentlyLoadingFileSuite)();

    if (suite) {
      if (typeof modifierArgs[0] === 'function') {
        suite._modifiers.push({
          type,
          fn: modifierArgs[0],
          location,
          description: modifierArgs[1]
        });
      } else {
        if (modifierArgs.length >= 1 && !modifierArgs[0]) return;
        const description = modifierArgs[1];

        suite._annotations.push({
          type,
          description
        });
      }

      return;
    }

    const testInfo = (0, _globals.currentTestInfo)();
    if (!testInfo) throw new Error(`test.${type}() can only be called inside test, describe block or fixture`);
    if (typeof modifierArgs[0] === 'function') throw new Error(`test.${type}() with a function can only be called inside describe block`);
    testInfo[type](...modifierArgs);
  }

  _setTimeout(timeout) {
    const suite = (0, _globals.currentlyLoadingFileSuite)();

    if (suite) {
      suite._timeout = timeout;
      return;
    }

    const testInfo = (0, _globals.currentTestInfo)();
    if (!testInfo) throw new Error(`test.setTimeout() can only be called from a test file`);
    testInfo.setTimeout(timeout);
  }

  _use(location, fixtures) {
    const suite = (0, _globals.currentlyLoadingFileSuite)();
    if (!suite) throw new Error(`test.use() can only be called in a test file`);
    suite._fixtureOverrides = { ...suite._fixtureOverrides,
      ...fixtures
    };
  }

  _extend(location, fixtures) {
    const fixturesWithLocation = {
      fixtures,
      location
    };
    return new TestTypeImpl([...this.fixtures, fixturesWithLocation]).test;
  }

  _declare(location) {
    const declared = new DeclaredFixtures();
    declared.location = location;
    const child = new TestTypeImpl([...this.fixtures, declared]);
    declared.testType = child;
    return child.test;
  }

}

exports.TestTypeImpl = TestTypeImpl;
const rootTestType = new TestTypeImpl([]);
exports.rootTestType = rootTestType;
//# sourceMappingURL=testType.js.map