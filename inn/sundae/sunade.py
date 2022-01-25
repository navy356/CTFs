# uncompyle6 version 3.5.0
# Python bytecode 2.7 (62211)
# Decompiled from: Python 3.6.8 (default, Nov 16 2020, 16:55:22)
# [GCC 4.8.5 20150623 (Red Hat 4.8.5-44)]
# Embedded file name: sundae.py
# Compiled at: 2021-12-02 14:19:57
syrup = 4
sauce = [66666, 55555, 44444, 33333]
cream = []
gg = []
mix = []


def cmp(a, b):
    return (a > b) - (a < b)


def Whipped_cream(Butter, key):
    for i in key:
        Butter[3] = (Butter[3] ^ i) - 117

    for i in key:
        Butter[6] = (Butter[6] ^ i) + 43

    for i in key:
        Butter[9] = (Butter[9] ^ i) - 99

    for i in key:
        Butter[12] = (Butter[12] ^ i) - 10

    for i in key:
        Butter[15] = (Butter[15] ^ i) + 115

    for i in key:
        Butter[18] = (Butter[18] ^ i) + 75

    for i in key:
        Butter[21] = (Butter[21] ^ i) - 22

    for i in key:
        Butter[24] = (Butter[24] ^ i) - 118

    for i in key:
        Butter[27] = (Butter[27] ^ i) + 38

    for i in key:
        Butter[30] = (Butter[30] ^ i) + 66

    for i in key:
        Butter[2] = (Butter[2] ^ i) - 117

    for i in key:
        Butter[4] = (Butter[4] ^ i) + 43

    for i in key:
        Butter[8] = (Butter[8] ^ i) - 99

    for i in key:
        Butter[10] = (Butter[10] ^ i) - 10

    for i in key:
        Butter[14] = (Butter[14] ^ i) + 115

    for i in key:
        Butter[16] = (Butter[16] ^ i) + 75

    for i in key:
        Butter[20] = (Butter[20] ^ i) - 22

    for i in key:
        Butter[22] = (Butter[22] ^ i) - 118

    for i in key:
        Butter[26] = (Butter[26] ^ i) + 38

    for i in key:
        Butter[28] = (Butter[28] ^ i) + 66

    return Butter

def Whipped_cream_rev(Butter, key):
    print('Butter_og',Butter)
    print(key)
    key.reverse()
    print(key)
    for i in key:
        Butter[3] = (Butter[3] + 117) ^ i

    for i in key:
        Butter[6] = (Butter[6] - 43) ^ i

    for i in key:
        Butter[9] = (Butter[9] + 99) ^ i

    for i in key:
        Butter[12] = (Butter[12] + 10) ^ i

    for i in key:
        Butter[15] = (Butter[15] - 115) ^ i

    for i in key:
        Butter[18] = (Butter[18] - 75) ^ i

    for i in key:
        Butter[21] = (Butter[21] + 22) ^ i

    for i in key:
        Butter[24] = (Butter[24] + 118) ^ i

    for i in key:
        Butter[27] = (Butter[27] - 38) ^ i

    for i in key:
        Butter[30] = (Butter[30] - 66) ^ i

    for i in key:
        Butter[2] = (Butter[2] + 117) ^ i

    for i in key:
        Butter[4] = (Butter[4] - 43) ^ i

    for i in key:
        Butter[8] = (Butter[8] + 99) ^ i

    for i in key:
        Butter[10] = (Butter[10] + 10) ^ i

    for i in key:
        Butter[14] = (Butter[14] - 115) ^ i

    for i in key:
        Butter[16] = (Butter[16] - 75) ^ i

    for i in key:
        Butter[20] = (Butter[20] + 22) ^ i

    for i in key:
        Butter[22] = (Butter[22] + 118) ^ i

    for i in key:
        Butter[26] = (Butter[26] - 38) ^ i

    for i in key:
        Butter[28] = (Butter[28] - 66) ^ i

    print('Butter', Butter)
    return Butter


def Vanilla_Essence():
    mix=[]
    for i in range(len(sauce)):
        mix.append(int(bin(sauce[i])[3:], 2) ^ 7)

    return mix


def CoCoAAA(inp):
    for i in range(0, len(inp)):
        cream.append((ord(inp[i]) & 15) >> 4 | ord(inp[i]) << 4)
        gg.append(cream[i] ^ 45)

    print(cream)
    return gg

def CoCoAAA_rev(inp):
    gg=[]
    cream=[]
    for i in range(0, len(inp)):
        #cream.append((ord(inp[i]) & 15) >> 4 | ord(inp[i]) << 4)
        cream.append(inp[i] ^ 45)
        gg.append(chr(cream[i]>>4))

    print(cream)
    return gg


def Chocolate_ice_cream(choco, syrup):
    result = []
    for i in range(len(choco)):
        char = choco[i]
        if char.isnumeric():
            result.append(chr(ord(char) + 2))
        elif char.isupper():
            result.append(chr((ord(char) + syrup - 65) % 26 + 65))
        elif char.islower():
            result.append(chr((ord(char) + syrup - 97) % 26 + 97))
        else:
            result.append(char)

    return result

def Chocolate_ice_cream_rev(choco, syrup):
    result = []
    for i in range(len(choco)):
        char = choco[i]
        if char>=chr(ord('0')+2) and char<=chr(ord('9')+2):
            result.append(chr(ord(char) - 2))
        elif char.isupper():
            result.append(chr((ord(char) - syrup - 65) % 26 + 65))
        elif char.islower():
            result.append(chr((ord(char) - syrup - 97) % 26 + 97))
        else:
            result.append(char)

    return result



def main():
    flag = 0
    #sugar = input('Enter ingredient:  ')
    sugar = 'ABUCBUCBWICBIWBCIWCBIWBCIWCBBBBBBBBBBBBBBBBBBBBB'
    bowl = open('book', 'r')
    Chocolate_Wafers = bowl.readlines()
    wafers=[]
    for wafer in Chocolate_Wafers:
        wafers.append(int(wafer[:-1],2))

    test=Whipped_cream_rev(wafers,Vanilla_Essence())
    test2=CoCoAAA_rev(test)
    test3=Chocolate_ice_cream_rev(test2,syrup)
    print(''.join(test3))
    if len(sugar) < 30:
        print('Did you even check out the code ? lol')
        exit()
    exit()
    CHOCOLATE_SUNDAE = Whipped_cream(
        CoCoAAA(Chocolate_ice_cream(sugar, syrup)), Vanilla_Essence())
    bowl = open('book', 'r')
    Chocolate_Wafers = bowl.readlines()
    for i in range(0, len(CHOCOLATE_SUNDAE)):
        if cmp(CHOCOLATE_SUNDAE[i], int(Chocolate_Wafers[i], 2)) == 0:
            flag += 1

    if flag == 31:
        print(
            'Good Work ! \nTake your flag : ', sugar)
    else:
        print('meh!')


if __name__ == '__main__':
    main()
