import re
nums=[]
with open('test.txt','r') as f:
    lines=f.readlines()
    for line in lines:
        line=line.strip()
        num = re.findall(r'_ZNSaIcEC1Ev\(0x7fff2ddf9def, .*?, .*?, (.*?)\)\s+= 0x7fff2ddf9def',line)
        nums.append(num[0])

print(nums)