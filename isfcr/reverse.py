def sign_extend(value,bits):
	sign_bit = 1 << (bits - 1)
	return (value & (sign_bit - 1)) - (value & sign_bit)

print("Enter a string: ")
s3 = input()
j = 0
s3_len = len(s3)
s2=[0,0,0,0,0,0,0,0,0,0]
s3_len_iter = s3_len
s3_len_iter = s3_len_iter - 2
while ( -1 < s3_len_iter):
	s2[s3_len_iter] = s3[j]
	s2[s3_len_iter + 1] = s3[j+1]
	j = j + 2
	s3_len_iter = s3_len_iter - 2

print(''.join(s2))

i = 0
s = list()
while True:
	num = sign_extend(i,8)
	s2_len = len(s2)
	if (s2_len <= num):
		break
	if (s2[i] < 'a' or 'z' < s2[i]):
		s.append(s2[i])
	else:
		if (s2[i] < '{'):
			tmp_char = chr(ord(s2[i]) - 0xf)
			s.append(tmp_char)
		else:
			tmp_char_2 = chr(ord(s2[i]) - ord('_'))
			tmp_int = 0x7a - ord(tmp_char_2)
			s.append(chr(tmp_int))
	i = i + 1

print(''.join(s))
