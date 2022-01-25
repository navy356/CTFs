(function(){
    window.app.ng.component('root',{
        templateUrl: '/scripts/components/root/template.html',
        controller: function($scope, apiService, $state, $window) {
            $scope.session = {
                user: null,
                logout: () => {
                    delete $window.localStorage['keypair[0]'];
                    delete $window.localStorage['keypair[1]'];
                    $window.location.reload()
                }
            };
            $scope.$on('session.start', (evt, user) => {
                $scope.session.user = user;
            });
            apiService.currentUser().then((data) => {
                $scope.session.user = data['user'];
            }, (response) => {
                $state.go('root.signup');
            });
        }
    });
})();
