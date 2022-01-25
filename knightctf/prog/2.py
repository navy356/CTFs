def algo():
    x=1
    answer=0

    while x<544:
        calculation = (x*(x+1)) + (2 *(x + 1))
        rev_calc = int(str(calculation)[::-1])

        if rev_calc%4 == 0:
            answer=answer+rev_calc

        x+=1

    print(answer)

algo()