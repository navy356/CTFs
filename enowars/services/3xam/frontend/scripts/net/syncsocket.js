(function() {
    class SyncSocket {
        constructor(socket) {
            this.socket = socket;
            this.nextMessageId = 0;
            this.pendingResponses = {}
        }
		async connect() {
			return this.socket.connect.apply(this.socket, arguments);
		}
        async loop() {
            while (true) {
                let incoming = await this.socket.recv();
                incoming = new TextDecoder().decode(incoming);
                incoming = JSON.parse(incoming);
                this.pendingResponses[incoming.id].resolve(incoming.data);
            }
        }
        send(data, timeout) {
            timeout = timeout || 30000;
            return new Promise((resolve, reject) => {
                let requestId = this.nextMessageId++;
                this.pendingResponses[requestId] = {
                    resolve: resolve,
                    reject: reject
                }
                this.socket.send(JSON.stringify({
                    id: requestId,
                    data: data
                }));
            });
        }
    }
	window.app.net.SyncSocket = SyncSocket;
})();
