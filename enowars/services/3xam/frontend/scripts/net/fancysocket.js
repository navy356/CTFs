(function(WebSocket){
    class FancySocket {
        constructor() {
            this.state = 0;
            this.recvResolve = null;
            this.recvReject = null;
            this.websocket = null;
        }
        async connect(address){
            let promise = new Promise((resolve, reject) => {
                if (this.state != 0)
                    throw "Must be in disconnected state";
 
                try {
                    this.websocket = new WebSocket(address);
                } catch (err) {
                    reject(err);
                    return;
                }
                this.websocket.binaryType = "arraybuffer"
                this.websocket.onmessage = (message) => {
                    let recvResolve = this.recvResolve;
                    this.recvResolve = null;
                    this.recvReject = null;
                    if (recvResolve != null) {
                        recvResolve(new Uint8Array(message.data));
                    }
                };
                this.state = 2;
                this.websocket.onopen = () => {
                    resolve();
                };
                this.websocket.onerror = function() {
                    reject();
                };
            });
            return promise;
        }

        send(message) {
            if (this.state != 2) {
                throw "Must be connected";
            }
            this.websocket.send(message);
        }

        async recv() {
            if (this.state != 2) {
                throw "Must be connected";
            }
            return new Promise((resolve, reject) => {
               this.recvResolve = resolve; 
               this.recvReject = reject;
            });
        }

    };
	window.app.net.FancySocket = FancySocket;
})(window.WebSocket || window.MozWebSocket);
