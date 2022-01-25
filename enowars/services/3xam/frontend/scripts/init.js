requirejs.config({
    baseUrl: 'scripts',
    paths: {
        vendor: '../vendor',
        'vendor.angular': '../vendor/angularjs/angular',
        'angular': '../vendor/angularjs/angular',
        'vendor.angular.route': '../vendor/angularjs/angular-route',
        //'vendor.angular-ui.router': '../vendor/angularjs/angular-ui-router',
    },
    shim: {
        'vendor.angular': {
            exports: 'angular',
            init: () => {
                window.name = "NG_DEFER_BOOTSTRAP!";
            }
        },
        'vendor.angular.route': ['vendor.angular'],
        'vendor/angularjs/angular-ui-router': ['vendor.angular'],
		'vendor.xhook': {
			exports: 'xhook'
		},
    }
});

define('WebSocket', () => {
    return window.WebSocket || window.MozWebSocket;
});
define('localStorage', () => {
    return window.localStorage;
});
define('vendor.xhook', () => {
    return window.xhook;
});

define('noisePromise', ['vendor/noise-c.wasm/index'], (createNoise) => {
    return createNoise((noise) => {
        define('vendor.noise', noise);
    });
});

requirejs(['noisePromise', 'vendor.xhook'], (noisePromise) => {
    Promise.all([noisePromise]).then(() => {
        requirejs(['vendor.angular', 'main'], (angular, app) => {
            var $html = angular.element(document.getElementsByTagName('html')[0]);
            angular.element().ready(function() {
                angular.bootstrap(document, [app.name]);
            });
        });
    });
});

