def getH(x):
    x = x//13
    h = 105*x+140
    return h

def getW(y):
    w = 105*y+140
    return w

def getResult(h,w):
    x = (h-140)//105*13
    y = (w-140)//105
    return x+y

def getHW(result):
    y = result%13
    x = result - y

    H=getH(x)
    Y=getW(y)

    return H,Y

print(getHW(1))