const a4_0x32f5 = ['mti2nta2m0T2BhHkua', 'z2v0vvjm', 'ndeZnZC5thLwwhLX', 'mtiXndu2ogL6zuzLua', 'mvDtAhPsqG', 'nZK0mdGZDe5sC1zN', 'r0vu', 'CNvUDgLTzq', 'B3bLBG', 'mtaXmZGWnhfsuvf0CW', 'uMnYEfG', 'CgfYC2u', 'nZaZuxnzu2PN', 'ntqXELLurwzX', 'mZqWnJqXmKrpDu52Dq', 'C2v0DgLUz3mUANnVBG'];

function a4_0x2ad4(_0x47cfb2, _0x8d4262) {
    _0x47cfb2 = _0x47cfb2 - 0xe5;
    let _0x32f5be = a4_0x32f5[_0x47cfb2];
    if (a4_0x2ad4['FEBRWq'] === undefined) {
        var _0x2ad47b = function(_0x304705) {
            const _0x397f55 = 'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789+/=';
            let _0x9da85c = '';
            for (let _0x460867 = 0x0, _0x3d3676, _0xc8d0ce, _0x12e8f7 = 0x0; _0xc8d0ce = _0x304705['charAt'](_0x12e8f7++); ~_0xc8d0ce && (_0x3d3676 = _0x460867 % 0x4 ? _0x3d3676 * 0x40 + _0xc8d0ce : _0xc8d0ce,
                    _0x460867++ % 0x4) ? _0x9da85c += String['fromCharCode'](0xff & _0x3d3676 >> (-0x2 * _0x460867 & 0x6)) : 0x0) {
                _0xc8d0ce = _0x397f55['indexOf'](_0xc8d0ce);
            }
            return _0x9da85c;
        };
        a4_0x2ad4['hEeiLk'] = function(_0x4112b2) {
                const _0x5c62a3 = _0x2ad47b(_0x4112b2);
                let _0x4e3933 = [];
                for (let _0x43fb08 = 0x0, _0x4e1f6b = _0x5c62a3['length']; _0x43fb08 < _0x4e1f6b; _0x43fb08++) {
                    _0x4e3933 += '%' + ('00' + _0x5c62a3['charCodeAt'](_0x43fb08)['toString'](0x10))['slice'](-0x2);
                }
                return decodeURIComponent(_0x4e3933);
            },
            a4_0x2ad4['DwOdNJ'] = {},
            a4_0x2ad4['FEBRWq'] = !![];
    }
    const _0x560c59 = a4_0x32f5[0x0],
        _0x2dc0f8 = _0x47cfb2 + _0x560c59,
        _0x3e394b = a4_0x2ad4['DwOdNJ'][_0x2dc0f8];
    return _0x3e394b === undefined ? (_0x32f5be = a4_0x2ad4['hEeiLk'](_0x32f5be),
            a4_0x2ad4['DwOdNJ'][_0x2dc0f8] = _0x32f5be) : _0x32f5be = _0x3e394b,
        _0x32f5be;
}
const a4_0x2fb0b7 = a4_0x2ad4;
(function(_0x33195c, _0x282102) {
    const _0xb52c48 = a4_0x2ad4;
    while (!![]) {
        try {
            const _0x141e24 = -parseInt(_0xb52c48(0xea)) + -parseInt(_0xb52c48(0xf3)) + parseInt(_0xb52c48(0xe6)) + -parseInt(_0xb52c48(0xf1)) * parseInt(_0xb52c48(0xe5)) + -parseInt(_0xb52c48(0xf4)) + parseInt(_0xb52c48(0xed)) * parseInt(_0xb52c48(0xee)) + parseInt(_0xb52c48(0xef));
            if (_0x141e24 === _0x282102)
                break;
            else
                _0x33195c['push'](_0x33195c['shift']());
        } catch (_0x29526f) {
            _0x33195c['push'](_0x33195c['shift']());
        }
    }
}(a4_0x32f5, 0xa4744));

function loadSettingsFromJson(_0x16a7bf) {
    const _0x278d93 = a4_0x2ad4,
        _0x834cd2 = {
            'RcrxX': '--\x20DOM\x20Invader:\x20Failed\x20to\x20parse\x20settings'
        };
    let _0x27a70b = new XMLHttpRequest();
    _0x27a70b[_0x278d93(0xe9)](_0x278d93(0xe7), _0x16a7bf, ![]),
        _0x27a70b['send'](null);
    try {
        return JSON[_0x278d93(0xec)](_0x27a70b['responseText']);
    } catch (_0xf54669) {
        console['error'](_0x834cd2[_0x278d93(0xeb)], _0xf54669);
    }
}
let settings = loadSettingsFromJson(chrome[a4_0x2fb0b7(0xe8)][a4_0x2fb0b7(0xf2)](a4_0x2fb0b7(0xf0)));