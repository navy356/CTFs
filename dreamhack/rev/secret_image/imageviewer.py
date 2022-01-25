import sys

# pip install pillow
from PIL import Image

if len(sys.argv) != 2:
    print(f"python {sys.argv[0]} <secretMessage.raw>")
else:
    with open(sys.argv[1], "rb") as f:
        output = f.read()
        Image.frombytes("1", (500, 50), output).show()
