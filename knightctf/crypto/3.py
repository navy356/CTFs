l = [chr(i) for i in range(65,91)]
key = open('code','r').read()

key = key.split(' ')

print(key)
flag = []
for i in key:
    val = i.count('.')

    print(val)
    flag.append(l[((val)-1)%26])

print(flag)