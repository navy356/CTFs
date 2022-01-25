(function(){
    window.app.ng.config(($logProvider, $stateProvider,
        $urlRouterProvider, $httpProvider) => {
            $logProvider.debugEnabled(true);
            $httpProvider.interceptors.push('apiOSDInterceptor');
            $stateProvider.state('root', {
                views: {
                    'layout': {
                        template: '<root></root>'
                    }
                },
                resolve: {
                    webTunnel: (webTunnel) => {
                        return webTunnel.run();
                    }
                }
            })
                .state('root.authed', {

                })
                .state('root.authed.dashboard', {
                    url: '/dashboard',
                    template: '<dashboard></dashboard>',
                })
                .state('root.signup', {
                    url: '/signup',
                    template: '<signup></signup>'
                })
                .state('root.authed.exam', {
                    abstract: true,
                    url: '/exams/:exam_id',
                    template: '<exam></exam>'
                })
                .state('root.authed.exam.scores_offset', {
                    url: '/scores/offset/:offset',
                    template: '<scores></scores>'
                })
                .state('root.authed.exam.scores', {
                    url: '/scores',
                    template: '<scores></scores>'
                })
                .state('root.authed.exam.question', {
                    url: '/:question_hash',
                    template: '<question></question>'
                });
            $urlRouterProvider.otherwise('dashboard');
        });
})();
