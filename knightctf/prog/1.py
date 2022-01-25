def algo():
    answer = 0
    x = 1
    y = 2
    while(x<667):
        answer+=(x*y)+((x*10)+y)
        x+=1

    print(answer)

algo()