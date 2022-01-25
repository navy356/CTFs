result = "ZRIU]HdANdJAGDIAxIAvDDsAyDDq_"
Upper_m = [chr(i) for i in range(65,77)]
Lower_m = [chr(i) for i in range(97,109)]
Upper_rest = [chr(i) for i in range(77,91)]
Lower_rest = [chr(i) for i in range(109,123)]
def decrypt():
    res= [chr(ord(i)-2) for i in result]
    flag = []
    for i in res:
        val = ord(i)

        if (chr(val - 13) in Upper_m) or (chr(val - 13) in Lower_m):
            flag.append(chr(val - 13)) 
        elif (chr(val + 13) in Upper_rest) or (chr(val + 13) in Lower_rest):
            flag.append(chr(val + 13)) 
        else:
            flag.append(chr(val+32))

    print(''.join(flag))

decrypt()