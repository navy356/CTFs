def sign_extend(value,bits):
	sign_bit = 1 << (bits - 1)
	return (value & (sign_bit - 1)) - (value & sign_bit)

s = ""
while (len(s) is not 10):
	print("Enter string of length 10: ")
	s = input()

i = 0
s2 = list()
while True:
	#if ( i == s_len):
	#	break
	num = sign_extend(i,8)
	s_len = len(s)
	if (s_len <= num):
		break
	if (s[i] < 'a' or 'z' < s[i]):
		s2.append(s[i])
	else:
		tmp_char = chr(ord(s[i]) + 0xf)
		if (tmp_char < '{'):
			s2.append(tmp_char)
		else:
			tmp_int = 0x7a - ord(s[i])
			tmp_char_2 = chr(tmp_int + ord('_'))
			s2.append(tmp_char_2)

	i = i + 1

j = 0
s2_str = ''.join(s2)
print(s2_str)
s2_len = len(s2_str)
s2_len_iter = s2_len
s3 = [0,0,0,0,0,0,0,0,0,0]
s2_len_iter = s2_len_iter - 2
while (-1 < s2_len_iter):
	s3[s2_len_iter] = s2[j]
	s3[s2_len_iter + 1] = s2[j + 1]
	j = j + 2
	s2_len_iter = s2_len_iter - 2

print(''.join(s3))
