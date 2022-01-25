(function(app){
    app.ng.factory('apiOSDInterceptor',
        ($q, notificationsService) => {
            function processResponseData(data) {
                if (data.log) {
                    console.log(data.log);
                }
                if (data.osd && data.osd.message) {
                    switch (data.osd.type) {
                        case 'error': {
                            notificationsService.error(data.osd.message);
                            break;
                        }
                        case 'success': {
                            notificationsService.success(data.osd.message);
                            break;
                        }
                        default: {
                            notificationsService.info(data.osd.message);
                        }
                    }
                }
            }
            return {
                'response': (response) => {
                    if (response.headers('content-type') 
                        == 'application/json') {
                        processResponseData(response.data);
                    }
                    return response;
                },
                'responseError': (rejection) => {
                    if (rejection.headers('content-type') 
                        == 'application/json') {
                        processResponseData(rejection.data);
                    }
                    return $q.reject(rejection);
                }};
        });
})(window.app);
