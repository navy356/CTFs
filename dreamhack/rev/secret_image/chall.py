def decrypt(filename,outputfilename):
    prev_ch=''
    ch = ''
    fp = open(outputfilename,'wb')
    with open(filename,'rb') as f:
        while True:
            prev_ch = ch
            ch = f.read(1)
            print(ch)
            if not ch:
                break
            if ch==prev_ch:
                ch = f.read(1)
                print(ch)
                if not ch:
                    break
                for i in range(int.from_bytes(ch,'big')+1):
                        fp.write(prev_ch)
            else:
                fp.write(ch)


if __name__ == "__main__":
    decrypt('msg.enc','msg.raw')