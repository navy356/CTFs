(function(){
    class ApiService {
        constructor($http) {
            this.$http = $http;
        }
        signup(name) {
            return this.$http.post('/users', {name: name})
                .then(resp => resp.data);
        }
        exams() {
            return this.$http.get('/exams')
                .then((resp) => resp.data);
        }
        getExam(examId) {
            return this.$http.get('/exams/' + examId)
                .then(resp => resp.data);
        }
        startExam(examId) {
            return this.$http.post('/exams/' + examId)
                .then(resp => resp.data);
        }
        answerQuestion(examId, questionHash, answer) {
            return this.$http.post('/exams/' + examId + '/' + questionHash,
                {'answer': answer}).then(resp => resp.data)
        }
        getExamQuestion(examId, questionHash) {
            return this.$http.get('/exams/' + examId + '/' + questionHash)
                .then(resp => resp.data);
        }
        currentUser() {
            return this.$http.get('/users/current')
                .then((resp) => resp.data);
        }
        scores(examId, offset, count) {
            let qString
            if (offset && count)
                qString = '?offset=' + offset + '&count=' + count
            else
                qString = ''
            return this.$http.get('/exams/' + examId + '/scores' + qString)
                .then((resp) => resp.data);
        }
    }
    window.app.ng.factory('apiService', ($http) => {
        return new ApiService($http);
    });
})();
