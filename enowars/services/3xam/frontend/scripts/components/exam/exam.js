(function(){
    window.app.ng.component('exam',{
        templateUrl: '/scripts/components/exam/template.html',
        controller: function($scope, $stateParams, apiService) {
            let examId = $stateParams.exam_id;
            apiService.getExam(examId).then((data) => {
                $scope.exam = data['exam'];
                $scope.number_of_questions = data['number_of_questions'];
            });
        }
    });
})();
