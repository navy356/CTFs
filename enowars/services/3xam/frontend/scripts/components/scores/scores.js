(function(){
    window.app.ng.component('scores',{
        templateUrl: '/scripts/components/scores/template.html',
        controller: async function($scope, $stateParams, apiService) {
            const pageSize = 20;
            let currPage = parseInt($stateParams.offset || 1);
            const maxPages = 8;
            const median = Math.ceil(maxPages/2)
            let startPage = currPage - median
            let endPage = currPage + median
            if (startPage <= 1) {
                startPage = 1
                endPage = maxPages;
            }
            apiService.scores($stateParams.exam_id, (currPage-1)*pageSize, pageSize).then((data) => {
                $scope.scores = data.scores;
                $scope.pages = [];

                const totalPages = Math.ceil(data.scores_count / pageSize);
                if (endPage > totalPages) {
                    endPage = totalPages;
                    startPage = endPage - maxPages;
                    startPage = startPage < 1 ? 1 : startPage;
                }

                $scope.pages = [
                    {
                        num: '<<',
                        url: `#!/exams/${$stateParams.exam_id}/scores/offset/1`,
                        disabled: currPage == 1
                    },
                    {
                        num: '<',
                        url: `#!/exams/${$stateParams.exam_id}/scores/offset/${currPage-1}`,
                        disabled: currPage <= 1
                    }
                ]

                for (let i = startPage; i <= endPage; i+=1) {
                    $scope.pages.push({
                        num: i,
                        url: `#!/exams/${$stateParams.exam_id}/scores/offset/${i}`
                        });
                    if (i == currPage) {
                        $scope.selected_page = i;
                    }
                }
                $scope.pages.push({
                    num: '>',
                    url: `#!/exams/${$stateParams.exam_id}/scores/offset/${currPage+1}`,
                    disabled: currPage > totalPages
                });
                $scope.pages.push({
                    num: '>>',
                    url: `#!/exams/${$stateParams.exam_id}/scores/offset/${totalPages}`,
                    disabled: currPage >= totalPages
                });
            });
        }
    });
})();
