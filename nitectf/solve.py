a, b, c, d = var('a b c d')
flag = 'nite'
enc = []
ciphertext = [8194393930139798, 7130326565974613, 9604891888210928, 6348662706560873, 11444688343062563, 7335285885849258, 3791814454530873, 926264016764633, 9604891888210928, 5286663580435343,
              5801472714696338, 875157765441840, 926264016764633, 2406927753242613, 5980222734708251, 5286663580435343, 2822500611304865, 5626320567751485, 3660106045179536, 2309834531980460, 12010406743573553]
for x, ct in zip(flag, ciphertext):
    res = (a*pow(ord(x), 3)+b*pow(ord(x), 2)+c*ord(x)+d) == ct
    enc.append(res)
ans = solve(enc, [a, b, c, d])
a, b, c, d = [i.rhs() for i in ans[0]]
flag = []
for ct in ciphertext:
    flag.append(solve(a*x**3+b*x**2+c*x+d == ct, x))
for i in flag:
    for j in i:
        try:
            print(chr(int(j.rhs())), end='')
        except:
            pass
