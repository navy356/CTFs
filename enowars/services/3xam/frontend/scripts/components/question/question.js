(function(){
    window.app.ng.component('question',{
        templateUrl: '/scripts/components/question/template.html',
        controller: function($scope, $stateParams, apiService, $state, notificationsService) {
            let examId = $stateParams.exam_id;
            let questionHash = $stateParams.question_hash;

            apiService.getExamQuestion(examId, questionHash).then((data) => {
                $scope.question = data['question'];
				$scope.question_index = data['question_index'];
            });
            $scope.submit = (() => {
                apiService.answerQuestion(examId, questionHash, $scope.answer)
                    .then((data) => {
                        if (data['next_question_hash'] != null) {
                            $state.go('.', {'question_hash': data['next_question_hash']});
                        } else {
                            notificationsService.success('Thank you for completing the exam');
                            $state.go('root.authed.dashboard');
                        }
                    }, (_) => {
                    });
            });
        }
    });
})();
