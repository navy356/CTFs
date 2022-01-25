import string
from Cursors import *
import sys 
from bcolors import *
import time

class Helpers:
    @staticmethod
    def binSearch(low: int, high: int, check, display: bool, **kwargs):
        mid = 0
        arr = kwargs.get('arr',None)
        i = kwargs.get('index',None)

        while low <= high:
            mid = (low+high)//2

            if arr:
                if display:
                    if arr[mid].isprintable():
                        sys.stdout.write(Cursors.SAVEC+Cursors.RESTOREC+Cursors.MOVEC(i+1)+bcolors.OKCYAN+arr[mid]+bcolors.ENDC+Cursors.RESTOREC)
                    else:
                        sys.stdout.write(Cursors.SAVEC+Cursors.RESTOREC+Cursors.MOVEC(i+1)+bcolors.WARNING+'!'+bcolors.ENDC+Cursors.RESTOREC)
                    sys.stdout.flush()
                if check(i,arr[mid])==1: #true is high
                    low = mid + 1

                elif check(i,arr[mid])==-1: #false is low
                    high = mid - 1

                else:
                    return mid

            else:
                if display:
                    sys.stdout.write("Trying {}\r".format(mid))
                    sys.stdout.flush()
                    sys.stdout.write(Cursors.ERASE_LINE)

                if check(mid)==1: #true is high
                    low = mid + 1

                elif check(mid)==-1: #false is low
                    high = mid - 1

                else:
                    return mid

        
        return -1

    @staticmethod
    def getOptions():
        dict={
            "warning" : True,
            "charset" : string.printable,
            "compareChar" : None,
            "checkChar" : None,
            "display" : True,
            "len" : -1,
            "compareLen" : None,
            "checkLen" : None,
            "maxLen" : 100,
            "minLen" : 0,
            "flag" : ''
        }
        return dict
