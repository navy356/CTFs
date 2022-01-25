(function() {
    class NoisySocket {
        constructor(fancySocket, noise) {
            this.fancySocket = fancySocket;
            this.connected = false;
            this.cipherstates = null;
			this.noise = noise;
        }

        async connect(address, keypair) {
            return new Promise(async (resolve, reject) => {
                await this.fancySocket.connect(address);
                let hs = this.noise.HandshakeState("Noise_XX_25519_AESGCM_SHA256",
                    this.noise.constants.NOISE_ROLE_INITIATOR);

                hs.Initialize(null, keypair[0], null, null);

                this.fancySocket.send(hs.WriteMessage());
                hs.ReadMessage(await this.fancySocket.recv());
                this.fancySocket.send(hs.WriteMessage());

                this.cipherstates = hs.Split();
                this.connected = true;
                resolve();
            }); 
        }

        send(message) {
            if (!this.connected)
                throw "NoisySocket not connected";
            this.fancySocket.send(
                this.cipherstates[0].EncryptWithAd(new Uint8Array(), message)
            );
        }

        async recv() {
            if (!this.connected)
                throw "Must be connected";
            return new Promise(async (resolve, reject) => {
                let data = await this.fancySocket.recv();
                resolve(this.cipherstates[1].DecryptWithAd(new Uint8Array(), data));
            });
        }
    }
	window.app.net.NoisySocket = NoisySocket;
})();
