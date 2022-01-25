import string
import itertools
from urllib.parse import quote_plus

while True:
    chars=range(65,1000)
	a = string.ascii_lowercase + string.ascii_uppercase
	target = input('\ntarget: ')
	test = [["",""],["","",""],["","","",""]]

	for d in target:
		for i, j in itertools.product(chars, repeat=2):
			a = chr(ord(i) ^ ord(j))
			if a == d:
				test[0][0]=test[0][0]+i
				test[0][1]=test[0][1]+j
				print(f"{i} ^ {j} == {a}")
				break

		for i, j, k in itertools.product(chars, repeat=3):
			a = chr(ord(i) ^ ord(j) ^ ord(k))
			if a == d:
				test[1][0]=test[1][0]+i
				test[1][1]=test[1][1]+j
				test[1][2]=test[1][2]+k
				print(f"{i} ^ {j} ^ {k} == {a}")
				break

		for i, j, k, l in itertools.product(chars, repeat=4):
			a = chr(ord(i) ^ ord(j) ^ ord(k) ^ ord(l))
			if a == d:
				test[2][0]=test[2][0]+i
				test[2][1]=test[2][1]+j
				test[2][2]=test[2][2]+k
				test[2][3]=test[2][3]+l
				print(f"{i} ^ {j} ^ {k} ^ {l} == {a}")
				break
	comb1=""
	for i in test:
		print("---------------------------------")
		for k in i:
			comb1=comb1+"'"+k+"'"+'^'
		print(quote_plus(comb1[0:len(comb1)-1]))
		print("---------------------------------")
		comb1=""
	#for a,b in test:
		#for i,j in a:
			#printf("{i} ^ {j}");
		#for i,j,k in b:
			#printf("{i} ^ {j} ^ {k}");
