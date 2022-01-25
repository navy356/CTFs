const dns = require('dns');
const { hostname } = require('os');

const validateURL = async(protocol, host) => {
    console.log(protocol);
    console.log(host);
    if (protocol !== 'http:') return [false];
    if (typeof host !== 'string') return [false];
    let ip = host;

    console.log(ip);

    if (/[^\d\.]/.test(host)) {
        try {
            ip = await new Promise((resolve, reject) => {
                dns.lookup(host, { family: 4 }, (err, address, family) => {
                    if (err) return reject(err);
                    if (family === 4) return resolve(address);
                    reject(new Error('Invalid ip.'));
                });
            });
        } catch {
            return [false];
        }
    }

    if (ip.startsWith('0')) return [false];
    if (ip.startsWith('127')) return [false];
    if (ip.startsWith('10.')) return [false];
    if (ip.startsWith('172')) return [false];

    return [true, ip];
};

const extractHostname = (url) => {
    let hostname;

    if (url.indexOf("//") > -1) {
        hostname = url.split('/')[2];
    } else {
        hostname = url.split('/')[0];
    }
    hostname = hostname.split('@')[0];
    hostname = hostname.split(':')[0];
    hostname = hostname.split('?')[0];
    return hostname;
}

module.exports = {
    validateURL,
    extractHostname,
};