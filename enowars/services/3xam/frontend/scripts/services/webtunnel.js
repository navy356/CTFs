(function(){
    /**
     * Loads keys Ok
     * Sets up socket connection via noisy socket Ok
     * Restores connection on disconnect
     * Singleton staying alive throughout the app instance
     * Configures http interceptor
     */
    function getKeys(noise) {
        let keypair = [2];
        let arrUtil = window.app.util.arr;
        keypair[0] = localStorage['keypair[0]'];
        keypair[1] = localStorage['keypair[1]'];
        if (keypair[0] == null || keypair[1] == null) {
            keypair = noise.CreateKeyPair(noise.constants.NOISE_DH_CURVE25519);
            localStorage['keypair[0]'] = arrUtil.toHex(keypair[0]);
            localStorage['keypair[1]'] = arrUtil.toHex(keypair[1]);
        } else {
            keypair[0] = arrUtil.fromHex(keypair[0]);
            keypair[1] = arrUtil.fromHex(keypair[1]);
        }
        return keypair;
    }

    function requestToString(request) {
        let raw = request.method + " " + request.url + "\n";
        for (const [key, value] of Object.entries(request.headers)) {
            raw += key + ": " + value + "\n";
        }
        raw += "\n";
        raw += request.body + "\n";
        return raw;
    }

    class WebTunnel {
        constructor($log, noise) {
            this.$log = $log;
            this.keypair = getKeys(noise);
            this.socket = new window.app.net.SyncSocket(
                new window.app.net.NoisySocket(
                    new window.app.net.FancySocket(), noise
                )
            );
            this.ignore = [/\/scripts\//];
            this.active = true;

            xhook.before(async (request, callback) => {
                for (const pattern of this.ignore) {
                    if (request.url.match(pattern)) {
                        callback();
                        return;
                    }
                }
                let resp = await this.socket.send(JSON.stringify({
                    'method': request.method,
                    'url': request.url,
                    'data': request.body,
                    'headers': request.headers
                }));
                let data = JSON.parse(resp);
                callback(data);
            });
        }
        async run() {
            try {
                await this.socket.connect('ws://' + window.location.hostname + ':8221/noise', this.keypair);
            } catch(err) {
                this.$log.error("Failed to establish connection, reason: " + err);
                return;
            }
            this.socket.loop();
        }
    }
    window.app.ng.factory('webTunnel', ($log, noise) => {
        return new WebTunnel($log, noise);
    })
})();
