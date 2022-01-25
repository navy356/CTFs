(function() {
    window.app.ng.component('signup', {
        templateUrl: '/scripts/components/signup/template.html',
        controller: function($scope, apiService, $state) {
            $scope.submit = function() {
                apiService.signup($scope.name).then((data) => {
                    $scope.$emit('session.start', data['user']);
                    $state.go('root.authed.dashboard');
                }, (response) => {
                    notificationsService.error('Something went wrong');
                    $scope.error = response.data.message;
                });
            }
        }
    });
})();
