(function(){
    class NotificationsService {
        constructor() {
            this.handlers = [];
            this.notification_id = 0;
        }
        addHandler(handler) {
            this.handlers.push(handler);
        }
        success(message) {
            this.dispatch(2, message);
        }
        info(message) {
            this.dispatch(0, message);
        }
        error(message) {
           this.dispatch(3, message); 
        }
        dispatch(type, message) {
            let id = ++this.notification_id;
            for (let i = 0; i < this.handlers.length; i++) {
                this.handlers[i]({
                    id: id,
                    message: message,
                    type: type 
                });
            }
        }
    }
    window.app.ng.factory('notificationsService', () => {
        return new NotificationsService();
    });
})();
