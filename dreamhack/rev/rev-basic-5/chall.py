from importlib.abc import ResourceLoader
import string

arr = [173, 216, 203, 203, 157, 151, 203, 196, 146, 161, 210, 215, 210, 214,
       168, 165, 220, 199, 173, 163, 161, 152, 76, 0, 0, 0, 0, 0, 0, 0, 0, 0]

result = [0]*(len(arr)+1)

def check(x,y):
    if chr(y) in string.printable and chr(x) in string.printable:
        return True
    return False

def summation(arr,x):
    tmp = arr.copy()
    tmp.reverse()
    sum=0   
    for i in range(0,len(tmp)):
        if(i%2==0):
            sum=sum+tmp[i]
        else:
            sum=sum-tmp[i]

    if(len(tmp)%2==0):
        sum=sum+x
    else:
        sum=sum-x

    return sum

def determineResult(n):
    res = [0]*(len(arr)+1)
    res[0]=-1
    res[1]=n+1
    while res[1]!=0:
        res[0]+=1
        res[1]-=1
        #print("New",res[0],res[1])
        flag=0
        if(check(res[0],res[1])):
            for i in range(2,len(arr)):
                res[i]=summation(arr[0:i],res[0])
                #print(res[i],summation(arr[0:i],res[0]),res[0])
                if(res[i]<0):
                    flag=1
                    break
        else:
            flag=1

        if flag==0:
            for i in res:
                print(chr(i),end='')

            print()
            break


determineResult(arr[0])