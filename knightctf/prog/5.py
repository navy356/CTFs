def G_sum(n):
    if n<0:
        return 0
    else:
        return 1/(pow(2,n))+G_sum(n-1)

    
print(G_sum(25))