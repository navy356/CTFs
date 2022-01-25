import itertools
import string
import hashlib
target = 'cb7a53dd721f4ca90b8fd3dbdabeda5a'
for i, j, k, l, m in itertools.product(string.ascii_lowercase+string.digits, repeat=5):
    d = b'shaktictf{' + (i+j+k+l+m).encode() + b'}'
    m = hashlib.md5(d).hexdigest()
    if m == target:
        print(d)
        break
