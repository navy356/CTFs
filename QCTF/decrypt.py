import random
import re

def decipher(text,key):
	key=list(key)
	text=list(text)
	random.shuffle(key)
	j=1
	for i in range(len(text)):
		text[i]=chr(int(text[i] - ord(key[j])))
		j = 3 if j<=0 else (j-1)

	string = ''.join(text)
	return string

test=[147,175,122,133,196,118,165,184,182,162,156,133,184,168,180,135,195,168]
key="QCTF"
answer=""
while True:
	answer=decipher(test,key)
	if re.search("^[a-zA-Z_0-9]+$",answer):
		print("qctf{"+answer+"}")
		break
