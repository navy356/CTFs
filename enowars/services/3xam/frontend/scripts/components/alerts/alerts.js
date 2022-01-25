(function(){
    window.app.ng.component('alerts',{
        templateUrl: '/scripts/components/alerts/template.html',
        controller: function($scope, $timeout, notificationsService) {
            let types = {
                0: 'primary',
                1: 'secondary',
                2: 'success',
                3: 'danger'
            };
            $scope.alerts = [];
            notificationsService.addHandler((data) => {
                $scope.alerts.push({
                    id: data.id,
                    type: types[data.type],
                    message: data.message
                });
                $timeout(() => {
                    for(let i = 0; i < $scope.alerts.length; i++) {
                        if($scope.alerts[i].id == data.id) {
                            $scope.alerts.shift(i);
                            break;
                        }
                    }
                }, 5000);
            });
        }
    });
})();
