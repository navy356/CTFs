import random
import subprocess


def is_hex(s):
    try:
        int(s, 16)
        return True
    except ValueError:
        return False


def run(shellcode, prob):
    p = subprocess.Popen(
        ["./runner", shellcode] + prob, stdout=subprocess.PIPE, stderr=subprocess.PIPE,
    )

    try:
        p.wait(1)
        returncode = p.poll()
        if returncode is None:
            print("TIMEOUT!")
            exit()
    except Exception as E:
        print("something wrong!")
        print(E)
        exit()

    return returncode


if __name__ == "__main__":
    print("input your shellcode(e.g. b812000000c3)")
    shellcode = input()

    if len(shellcode) % 2 != 0 or not is_hex(shellcode):
        print("need hex string!")
        exit()

    for _ in range(10):
        argc = random.randint(10, 20)
        argv = [random.randint(10, 99) for _ in range(argc)]

        answer = 0
        for arg in argv:
            if arg % 3 == 0:
                answer += arg
            else:
                answer += arg * 2

        answer = answer % 100

        argv = list(map(str, argv))

        returncode = run(shellcode, argv)

        if answer != returncode:
            print("wrong!")
            print("prob:", argv)
            print("answer:", answer)
            print("your answer:", returncode)
            exit()
    
    with open("flag") as f:
        print(f.read())
