var missing_one=['log','3_y'];
(function(a, b) {
    var c = function(d) {
        while (--d) {
            a['push'](a['shift']());
        }
    };
    c(++b);
}(missing_one, 0x71));
var val = function(param1, param2) {
    param1 = param1;
    var val_1 = missing_one[param1];
    return val_1;
};
var _0x5948 = function(_0x59c077, _0x43efff) {
    _0x59c077 = _0x59c077 - 0x0;
    var _0x14ee75 = missing_one[_0x59c077];
    if (_0x5948['nYXctf'] === undefined) {
        var _0x594891 = function(_0x575770) {
            var _0x5bf418 = 'abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789+/=',
                _0x2562dd = String(_0x575770)['replace'](/=+$/, '');
            var _0x2b8d13 = '';
            for (var _0x163295 = 0x0, _0x505835, _0x100b31, _0x12266f = 0x0; _0x100b31 = _0x2562dd['charAt'](_0x12266f++); ~_0x100b31 && (_0x505835 = _0x163295 % 0x4 ? _0x505835 * 0x40 + _0x100b31 : _0x100b31, _0x163295++ % 0x4) ? _0x2b8d13 += String['fromCharCode'](0xff & _0x505835 >> (-0x2 * _0x163295 & 0x6)) : 0x0) {
                _0x100b31 = _0x5bf418['indexOf'](_0x100b31);
            }
            return _0x2b8d13;
        };
        var _0x5b460f = function(_0x278d9c, _0x5b848e) {
            var _0x40b45e = [],
                _0xbd9ddb = 0x0,
                _0x3b736c, _0x24fe87 = '',
                _0x297a0e = '';
            _0x278d9c = _0x594891(_0x278d9c);
            for (var _0x7aa4b4 = 0x0, _0xecfba1 = _0x278d9c['length']; _0x7aa4b4 < _0xecfba1; _0x7aa4b4++) {
                _0x297a0e += '%' + ('00' + _0x278d9c['charCodeAt'](_0x7aa4b4)['toString'](0x10))['slice'](-0x2);
            }
            _0x278d9c = decodeURIComponent(_0x297a0e);
            var _0x24d1cb;
            for (_0x24d1cb = 0x0; _0x24d1cb < 0x100; _0x24d1cb++) {
                _0x40b45e[_0x24d1cb] = _0x24d1cb;
            }
            for (_0x24d1cb = 0x0; _0x24d1cb < 0x100; _0x24d1cb++) {
                _0xbd9ddb = (_0xbd9ddb + _0x40b45e[_0x24d1cb] + _0x5b848e['charCodeAt'](_0x24d1cb % _0x5b848e['length'])) % 0x100, _0x3b736c = _0x40b45e[_0x24d1cb], _0x40b45e[_0x24d1cb] = _0x40b45e[_0xbd9ddb], _0x40b45e[_0xbd9ddb] = _0x3b736c;
            }
            _0x24d1cb = 0x0, _0xbd9ddb = 0x0;
            for (var _0x717542 = 0x0; _0x717542 < _0x278d9c['length']; _0x717542++) {
                _0x24d1cb = (_0x24d1cb + 0x1) % 0x100, _0xbd9ddb = (_0xbd9ddb + _0x40b45e[_0x24d1cb]) % 0x100, _0x3b736c = _0x40b45e[_0x24d1cb], _0x40b45e[_0x24d1cb] = _0x40b45e[_0xbd9ddb], _0x40b45e[_0xbd9ddb] = _0x3b736c, _0x24fe87 += String['fromCharCode'](_0x278d9c['charCodeAt'](_0x717542) ^ _0x40b45e[(_0x40b45e[_0x24d1cb] + _0x40b45e[_0xbd9ddb]) % 0x100]);
            }
            return _0x24fe87;
        };
        _0x5948['uytZNF'] = _0x5b460f, _0x5948['ngVVfy'] = {}, _0x5948['nYXctf'] = !![];
    }
    var _0x19c019 = _0x5948['ngVVfy'][_0x59c077];
    return _0x19c019 === undefined ? (_0x5948['qtLhBJ'] === undefined && (_0x5948['qtLhBJ'] = !![]), _0x14ee75 = _0x5948['uytZNF'](_0x14ee75, _0x43efff), _0x5948['ngVVfy'][_0x59c077] = _0x14ee75) : _0x14ee75 = _0x19c019, _0x14ee75;
};
var test = val,
    s = 'AF',
    sn = 'FC',
    sm = 'TF{',
    sp = 'h3r',
    sd = test('0x0'),
    sl = '0u',
    sk = '_',
    sj = 'g0',
    st = '}';

function getData(flag = !![]) {
    var consol_var = test;
    if (flag) {
        var ans = s + sn + sm + sp + sd + sl + sk + sj + st;
        console[consol_var('0x1')](ans);
    }
}
getData(false);