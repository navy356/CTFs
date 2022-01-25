def decrypt(flag):
    flag = list(flag)
    for i in range(len(flag)-1,-1,-1):
        for j in range(len(flag)-2,i-1,-1):
            x = flag[j]
            flag[j] = flag[j+1]
            flag[j+1] = x

    print(''.join(flag))


decrypt("CFb5cp0rm1gK{1r4nT_m4}6")