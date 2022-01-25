from ctypes import CDLL
from math import floor
import time

libc = CDLL("./libc.6")
now = int(floor(time.time()))

libc.srand(now)

print(libc.rand())