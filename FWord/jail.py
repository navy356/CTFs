import sys

if (len(sys.argv) < 2):
	sys.exit("Usage: python3 jail.py <string to convert>")

for char in sys.argv[1]:
	num = ord(char)
	if char.isupper():
		print("$__=$_;")
		while num > ord('A'):
			print("$__++;",end="")
			num = num - 1
		print()
		print("$___.=$__;")
		print()
	else:
		print("$__=$____;")
		while num > ord('a'):
			print("$__++;",end="")
			num = num - 1
		print()
		print("$___.=$__;")
		print()
