result = "*9J<qiEUoEkU]EjUc;U]EEZU`EEXU^7fFoU^7Y*_D]s"

def decrypt():
    flag = []

    for i in result:
        val = ord(i)
        if val==42:
            val=65
        val+=10
        flag.append(chr(val))

    print(''.join(flag))

decrypt()