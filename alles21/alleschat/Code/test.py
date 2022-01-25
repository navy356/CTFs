import pyautogui
import time
import pyperclip

API_URL='http://127.0.0.1:1024'

def openApp(username,password):
    pyautogui.hotkey('ctrl', 'shift', 't')
    pyautogui.write('cd /home/navy356/CTFs/alles21/alleschat/app')  
    pyautogui.press('enter')
    pyautogui.write('npm start')
    pyautogui.press('enter')
    time.sleep(5)
    pyautogui.press('tab')
    pyautogui.write(API_URL)
    pyautogui.press('tab')
    pyautogui.write(username)
    pyautogui.press('tab')
    pyautogui.write(password)
    print(pyautogui.locateCenterOnScreen('Images/login.png'))
    pyautogui.click(pyautogui.locateCenterOnScreen('Images/login.png'))
    time.sleep(2)
    if pyautogui.locateOnScreen('Images/invalid.png'):
        print('!!User not found, registering')
        pyautogui.click(pyautogui.locateCenterOnScreen('Images/register.png'))
        return 0

    return 1

def sendTestMessage():
    pyautogui.press('tab')
    pyautogui.press('enter')
    pyautogui.click(pyautogui.locateCenterOnScreen('Images/usernameField.png'))
    pyautogui.write('test')
    pyautogui.click(pyautogui.locateCenterOnScreen('Images/messageField.png'))
    pyautogui.write('test')
    pyautogui.click(pyautogui.locateCenterOnScreen('Images/send.png'))

def sendMessage(message):
    pyautogui.click(pyautogui.locateCenterOnScreen('Images/messageField.png'))
    for ch in message:
        if ch=="<":
            pyperclip.copy(ch)
            pyautogui.hotkey('ctrl', 'v', interval=0.1)
        else:
            pyautogui.press(ch)
    pyautogui.click(pyautogui.locateCenterOnScreen('Images/send.png'))

def setIsDev():
    sendMessage("<p id=\"isDev\"></p>")

def main():
    isDev=openApp('navy356v3','test')
    sendTestMessage()
    if isDev==0:
        setIsDev()
    while True:
        message=pyautogui.prompt(text='Enter Message: ', title='Message' , default='')
        if message is not None:
            sendMessage(message)
        else:
            break

main()