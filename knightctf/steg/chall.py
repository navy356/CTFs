from qrtools import QR
import glob, os
import re
os.chdir("./qr2")
flag = [0]*48
for file in glob.glob("*.png"):
    index=re.findall(r"QR_Code_From_The_Future-(.*).png",file)
    index=int(index[0])
    myCode = QR(filename=file)
    if myCode.decode():
        flag[index]=myCode.data

print(flag)
print(''.join(flag))