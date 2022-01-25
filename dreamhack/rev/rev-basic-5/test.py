def summation(arr,x):
    tmp = arr.copy()
    tmp.reverse()
    print(tmp)
    sum=0   
    for i in range(0,len(tmp)):
        if(i%2==0):
            sum=sum+tmp[i]
        else:
            sum=sum-tmp[i]

    if(len(tmp)%2==0):
        sum=sum-x
    else:
        sum=sum+x

    return sum

arr=[1,2,3]
x=1
print(summation(arr,x))