from turtle import right


arr = [82, 223, 179, 96, 241, 139, 28, 181, 87, 209, 159, 56, 75, 41, 217, 38, 127, 201, 163, 233, 83, 24, 79, 184, 106, 203, 135, 88, 91, 57, 30, 0]

result = [0]*len(arr)

INT_BITS = 8

def leftRotate(n, d):

    # In n<<d, last d bits are 0.
    # To put first 3 bits of n at 
    # last, do bitwise or of n<<d
    # with n >>(INT_BITS - d) 
    return (n << d)|(n >> (INT_BITS - d))

def rightRotate(n, d):

    # In n>>d, first d bits are 0.
    # To put last 3 bits of at 
    # first, do bitwise or of n>>d
    # with n <<(INT_BITS - d) 
    return (n >> d)|(n << (INT_BITS - d)) & 0xFF

def main():
    for i in range(0,len(arr)):
        result[i] = rightRotate(arr[i]^i,i&7)

    flag = [chr(i) for i in result]
    print(''.join(flag))

if __name__ == "__main__":
    main()