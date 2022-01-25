(function(){

const fromHex = hexString =>
      new Uint8Array(hexString.match(/.{1,2}/g).map(byte => parseInt(byte, 16)));
const toHex = arr =>
        [].slice.call(arr).map(x => x.toString(16).padStart(2, '0')).join('');

    window.app.util.arr = {
        'fromHex': fromHex,
        'toHex': toHex
    };
})();
