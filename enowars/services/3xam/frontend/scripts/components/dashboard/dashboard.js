(function(){
    window.app.ng.component('dashboard',{
        templateUrl: '/scripts/components/dashboard/template.html',
        controller: function(apiService, $scope, $state, notificationsService) {
            apiService.exams()
                .then((data) => {
                    $scope.exams = data['exams'];
                }, (response) => {
                    $state.go('root.signup');
                });

            $scope.start = (exam) => {
                apiService.startExam(exam.id).then((data) => {
                    $state.go('root.authed.exam.question',
                        {'exam_id': exam.id, 'question_hash': data['next_question_hash']});
                }, (response)=>{
                    notificationsService.error(response.data.message);
                });
            }
        }
    });
})();
