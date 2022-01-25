import string
import itertools

while True:
	chars = '0123456789+-*/().~^|&'
	a = string.ascii_lowercase + string.ascii_uppercase
	target = input('\ntarget: ')
	test = [["",""],["","",""]]

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
	comb1=""
	for i in test:
		print("---------------------------------")
		for k in i:
			comb1=comb1+"'"+k+"'"+'^'
		print(comb1[0:len(comb1)-1])
		print("---------------------------------")
		comb1=""
	#for a,b in test:
		#for i,j in a:
			#printf("{i} ^ {j}");
		#for i,j,k in b:
			#printf("{i} ^ {j} ^ {k}");
