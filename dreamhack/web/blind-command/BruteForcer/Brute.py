from typing import DefaultDict
from Helpers import *
from bcolors import *
from Cursors import *
import time
import sys
from concurrent.futures import ThreadPoolExecutor

class Brute:
    def __init__(self,**kwargs):
        DefaultOptions = Helpers.getOptions()

        #warning mode - True to show warning messages, False otherwise
        self.warning = kwargs.get('warning',DefaultOptions['warning'])

        #charset to bruteforce. Default is ascii printable characters
        self.charset = kwargs.get('charset', DefaultOptions['charset'])

        #efficient function to bruteforce character at index n
        self.compareChar = kwargs.get('compareChar',DefaultOptions['compareChar'])

        #default function to bruteforce character at index n
        self.checkChar = kwargs.get('checkChar',DefaultOptions['checkChar'])

        #one function to bruteforce character must exist
        if not self.checkChar and not self.compareChar:
            raise Exception('No function to check character provided')

        #display bruteforce progress
        self.display = kwargs.get('display',DefaultOptions['display'])

        #length of string to bruteforce
        self.len = kwargs.get('len',DefaultOptions['len'])

        #default function to get length of string to bruteforce
        self.checkLen = kwargs.get('checkLen',DefaultOptions['checkLen'])

        #efficient function to get length of string to bruteforce
        self.compareLen = kwargs.get('compareLen',DefaultOptions['compareLen'])

        #one function to bruteforce length must exist if length is not provided
        if not self.compareLen and not self.checkLen and not self.len:
            raise Exception("Length or a function to check length must be provided")

        #maximum length of string to bruteforce
        self.maxLen = kwargs.get('maxLen',DefaultOptions['maxLen'])

        #minimum length of string to bruteforce
        self.minLen = kwargs.get('minLen',DefaultOptions['minLen'])

        #part of flag if obtained alredy
        self.flag = kwargs.get('flag',DefaultOptions['flag'])

    def setLen(self,l):
        self.len=l

    def getLen(self):
        if self.len>0:
            return


        elif self.compareLen:
            while True:
                if self.compareLen:
                    self.len=Helpers.binSearch(self.minLen, self.maxLen, self.compareLen, self.display)

                if self.len!=-1:
                    break

                if self.warning:
                    print(bcolors.WARNING+'!!Could not find length in range({},{})'.format(self.minLen,self.maxLen)+bcolors.ENDC)

                    if not (self.minLen<100):
                        self.minLen-=100
                    else:
                        self.minLen=0

                    self.maxLen+=100

                if self.warning:
                    print(bcolors.WARNING+'!!Checking in range({},{})'.format(self.minLen,self.maxLen)+bcolors.ENDC)
            
        else:
            for i in range(self.minLen,self.maxLen):
                if self.display:
                    sys.stdout.write(bcolors.OKCYAN+'++Trying length: {}\r'.format(i)+bcolors.ENDC)
                    sys.stdout.flush()
                    sys.stdout.write(Cursors.ERASE_LINE)
                if self.checkLen(i):
                    self.len=i
                    break

            return


    def getCharAt(self,i):
        ch=bcolors.WARNING+"?"+bcolors.ENDC
        if self.compareChar:
            sortedCharset="".join(sorted(self.charset))
            j=Helpers.binSearch(0,len(self.charset)-1,self.compareChar,self.display,arr=sortedCharset,index=i)
            if j!=-1:
                ch=sortedCharset[j]
            else:
                ch="?"
        
        else:
            for j in self.charset:
                if self.display:
                    if j.isprintable():
                        sys.stdout.write(Cursors.SAVEC+Cursors.RESTOREC+Cursors.MOVEC(i+1)+bcolors.OKCYAN+j+bcolors.ENDC+Cursors.RESTOREC)
                    else:
                        sys.stdout.write(Cursors.SAVEC+Cursors.RESTOREC+Cursors.MOVEC(i+1)+bcolors.WARNING+'!'+bcolors.ENDC+Cursors.RESTOREC)
                    sys.stdout.flush()
                    time.sleep(0.1)
                if (self.checkChar(i,j)):
                    ch=j
                    break

        sys.stdout.write(Cursors.SAVEC+Cursors.RESTOREC+Cursors.MOVEC(i+1)+bcolors.OKGREEN+ch+bcolors.ENDC+Cursors.RESTOREC)
        sys.stdout.flush()
        return ch

    def run(self,n):
        self.getLen()
        flag=list()
        if self.display:
            print(bcolors.OKGREEN+'++Length is {}'.format(self.len)+bcolors.ENDC)
            if len(self.flag)>0:
                print(bcolors.OKCYAN+'++Partial flag given is {}'.format(self.flag)+bcolors.ENDC)
            print(bcolors.OKCYAN+'++Trying'+bcolors.ENDC,end='')
            print(Cursors.SAVEC,end='')
            if len(self.flag)>0:
                print(bcolors.OKGREEN+' '+self.flag+bcolors.ENDC,end='')
            else:
                print(' ',end='')
            print(bcolors.WARNING+'?'*(self.len-len(self.flag))+bcolors.ENDC,end='')
            print(Cursors.RESTOREC,end='')

        with ThreadPoolExecutor(max_workers=n) as executor:
            if self.compareChar:
                flag = executor.map(self.getCharAt,[*range(len(self.flag),self.len)])
            else:
                flag = executor.map(self.getCharAt,[*range(len(self.flag),self.len)])

        sys.stdout.write("\r"+bcolors.OKGREEN+'++Trying'+bcolors.ENDC)
        sys.stdout.flush()
        print()
        sys.stdout.write(Cursors.ERASE_LINE)
        sys.stdout.flush()

        self.flag = self.flag + ''.join(flag)
        return(self.flag)