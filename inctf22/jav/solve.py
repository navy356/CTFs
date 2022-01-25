l = 6
def lrotate(arr,n):
    temp = []
    i = 0
    d=len(arr)

    while (i < n):
        temp.append(arr[i])
        i = i + 1
    i = 0
    while (n < d):
        arr[i] = arr[n]
        i = i + 1
        n = n + 1
    arr[:] = arr[: i] + temp
    return arr

def get_array(arr,n,n2):
    arr2 = [0]*(l*2)
    n3=0
    for i in range(n,n2):
        for j in range(0,l):
            arr2[n3]=arr[i][j]
            n3+=1

    return arr2

def shuffle1(arr):
    n=0
    n2=l*2-1
    arr2 = [0]*(l*2)
    for i in range(0,l*2,2):
        arr2[i]=arr[n2]
        arr2[i+1]=arr[n]
        n2-=1
        n+=1

    return arr2

def shuffle2(s,n):
    charArray = list(s)
    n2 = n // 2
    n3 = n2 - 1
    arr = [0]*n
    arr2 = [0]*n

    for i in range(0,n,2):
        arr[i]=charArray[n3]
        arr[i+1]=charArray[n2]
        n3-=1
        n2+=1

    for j in range(0,n):
        arr2[j]=arr[n - (j ^ 0x1) - 1]

    return arr2

def solve(s):
    n=0
    rows, cols = (l, l)
    arr = [[0]*cols]*rows   
    str = [0]*36

    for i in range(0,l):
        for j in range(0,l):
            if(i%2==0):
                arr[i][j]=s[n]
            else:
                arr[l-j-1][i]=s[n]
            n+=1

    lrotate(shuffle1(get_array(arr, 0, 1)), 7), lrotate(shuffle1(get_array(arr, 2, 3)), 8), lrotate(shuffle1(get_array(arr, 4, 5)), 10))