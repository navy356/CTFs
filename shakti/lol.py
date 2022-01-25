test = "lcZdl_Yoati+Xjn,lN!gGRdNR-R]H`=XjN,lo*+Iv"
list1 = []
count = 0

while (count < len(test)):
	if ((6 < count) and (count < 0x11)):
		tmp = test[count]
		list1.append(tmp)
		list1[count]=chr(ord(tmp+1))
	if ((3 < count) and (count < 7)):
		list1.append(chr(ord(test[count]) + 3))
	if ((count < 0x1e)  and (0x10 < count)):
		list1.append(chr(ord(test[count]) ^ 4))
	else:
		list1.append(chr(ord(test[count]) - 5))
	count = count + 1

testlol=""
for c in list1:
	testlol=testlol+c

print(testlol)

count = 0
list2=[]
while (count < len(testlol)):
	list2.append(chr((ord(testlol[count]) + 5) ^ 1))
	count = count + 1

testf=""
for c in list2:
	testf=testf + c

print(testf)