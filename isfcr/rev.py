i=0
j=3
input_text="0123456789012345678"
inp=list()
for c in input_text:
	inp.append(c)
var2=list()
while (i < 19) :
	tmp = j + ord(inp[i]) - 0x61
	#var2[i] = chr(tmp) + chr(tmp/26) * -26 + 'a'
	var2.append(chr(tmp + int(tmp/26) * -26 + ord('a')))
	#print(var2)
	j = j + 6
	i = i + 1
#print(var2)
k = 0
var3 = list()
arr = [0x21,0x3f,0xc,0x35,4,0x11,0xf,0x16,0x2b,0x19,0x1f,0x58,0x11,7,0x27,0x1c,4,0x2a,0x12]
while (k < 19) :
	var3.append(chr(arr[k] ^ ord(var2[k])))
	k = k + 1
	#print(var3[k])
print(''.join(var3))

