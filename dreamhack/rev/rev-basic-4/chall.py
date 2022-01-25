arr = [36, 39, 19,198,198,19, 22, 230, 71, 245, 38, 150,71, 245, 70, 39, 19,38,38,198, 86, 245,195,195,245,227,227,0,0,0,0,0]

result = [0]*len(arr)

for i in range(0,0x1c):
    result[i]=(arr[i]<<4&0xff)|(arr[i]>>4)
    #print(arr[i],result[i])

for i in result:
    print(chr(i),end='')

print()