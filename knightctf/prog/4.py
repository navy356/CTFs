import math

def getSum(n):
     
    strr = str(n)
    list_of_number = list(map(int, strr.strip()))
    return sum(list_of_number)

def ans(x,y):
    gcd = math.gcd(x,y)

    return getSum(gcd)*1234

print(ans(21525625,30135875))