import string

arr = [172, 243, 12, 37, 163, 16, 183, 37, 22, 198, 183, 188, 7,
       37, 2, 213, 198, 17, 7, 197, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0]

result = [0]*len(arr)

def findByte(x):
    for i in string.printable:
        num = ord(i)
        if ((-5*num)&0xff)==x:
            return num

    return 0


def main():
    for i in range(0,len(arr)):
        result[i]=findByte(arr[i])

    print(result)

    flag = [chr(i) for i in result]

    print(''.join(flag))

if __name__ == "__main__":
    main()