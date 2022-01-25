var sha1 = ['svgForm', 'elements', 'onchange', 'onload', 'target', 'readyState', 'error', 'Error\x20while\x20reading\x20file', 'result', 'parseFromString', 'image/svg+xml', 'Modal', 'getElementById', 'resultModal', 'resultModalBody', 'secret.txt', 'then', 'text', 'getElementsByTagName', 'path', 'getAttribute', 'innerText', 'The\x20logo\x20is\x20legit!', 'show', 'The\x20logo\x20is\x20counterfeit!', 'readAsText', 'files', 'length', 'substring'];
var base64 = function(index) {
    return sha1[index];
};
var sHa512 = base64;
document['forms'][sHa512(0x0)][sHa512(0x1)]['svgFile'][sHa512(0x2)] = function(md4) {
    var bAse64 = base64,
        sha512 = new FileReader();
    sha512[bAse64(0x3)] = function(sha256) {
        var aEs = base64;
        if (sha256[aEs(0x4)][aEs(0x5)] != 0x2) return;
        if (sha256['target'][aEs(0x6)]) {
            alert(aEs(0x7));
            return;
        }
        RSA = sha256[aEs(0x4)][aEs(0x8)];
        const base58 = new DOMParser(),
            aes = base58[aEs(0x9)](RSA, aEs(0xa));
        var Sha512 = new bootstrap[(aEs(0xb))](document[aEs(0xc)](aEs(0xd))),
            Aes = document[aEs(0xc)](aEs(0xe));
        fetch(aEs(0xf))[aEs(0x10)](Base64 => Base64[aEs(0x11)]())[aEs(0x10)](Base58 => {
            var bAse58 = base64;
            encoded = base65(aes[bAse58(0x12)](bAse58(0x13))[0x0][bAse58(0x14)]('d'))
            ,encoded === Base58 ? (Aes[bAse58(0x15)] = bAse58(0x16), Sha512[bAse58(0x17)]()) : (Aes['innerText'] = bAse58(0x18), Sha512['show']());
        });
    }, sha512[bAse64(0x19)](md4[bAse64(0x4)][bAse64(0x1a)][0x0]);
};
const base65 = Sha1 => {
    var mD4 = base64;
    let Sha256 = '',
        Md4 = '';
    for (var sHa1 = 0x0; sHa1 < Sha1[mD4(0x1b)]; sHa1++) {
        Sha256 += MD5(Md4 + Sha1[sHa1])[mD4(0x1c)](0x0, 0x4), Md4 += Sha1[sHa1];
    }
    return Sha256;
};

const func = function(Sha1)
{
    let Sha256 = '', Md4='';
    for(var i=0; i < Sha1.length; i++)
    {
        Sha256 += MD5(Md4+Sha1[i])['substring'](0,4);
        Md4 += Sha1[i];
    }
    return Sha256;
}