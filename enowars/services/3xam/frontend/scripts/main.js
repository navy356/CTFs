(function(){
    window.app = {
        ng: null,
        net: {},
        util: {}
    };
    window.app.ng = angular.module('examApp', ['ui.router']);
    noise_c_wasm((noise) => {
        window.app.ng.value('noise', noise);
        angular.element(() => {
            angular.bootstrap(document, ['examApp']);
        });
    });
})();
