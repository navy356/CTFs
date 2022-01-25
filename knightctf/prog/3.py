def sumSquare(n):
 
    s = dict()
    for i in range(n):
 
        if i * i > n:
            break
 
        # store square value in hashmap
        s[i * i] = 1
 
        if (n - i * i) in s.keys():
            print((n - i * i)**(1 / 2),
                       "^2 +", i, "^2")
            return True
         
    return False
 
# Driver Code
n = 25000
if n==1:
  print('0^2 + 1^2')
elif (sumSquare(n)):
    print("Yes")
else:
    print("No")
 