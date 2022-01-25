class Cursors:
    SAVEC = '\033[s'
    RESTOREC = '\033[u'
    CURSOR_UP_ONE = '\033[F' 
    ERASE_LINE = '\033[K' 

    @staticmethod
    def MOVEC(n):
        return '\033[{}C'.format(n)