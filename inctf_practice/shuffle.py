import requests

flag_s="_g00d}1s_n0t0p1ng_uch_l0{s00_minctfj"

flag_shuffled=list(flag_s)
def unshuffle():
    flag=""

    for i in range(0x1e,0x1e+6):
        flag=flag+flag_shuffled[i]

    for i in range(0x18,0x18+6):
        flag=flag+flag_shuffled[i]

    for i in range(0x12,0x12+6):
        flag=flag+flag_shuffled[i]

    for i in range(0xc,0xc+6):
        flag=flag+flag_shuffled[i]

    for i in range(6,6+6):
        flag=flag+flag_shuffled[i]

    for i in range(0,0+6):
        flag=flag+flag_shuffled[i]
    print(flag)

unshuffle()