from playwright.sync_api import sync_playwright
import time
import os
import binascii

UPLOAD_FOLDER="/home/navy356/CTFs/MyChalls/Chall1/flag/"

def makeFlagFile():
    flagDIR=binascii.b2a_hex(os.urandom(5)).decode("utf-8")
    os.mkdir(UPLOAD_FOLDER+flagDIR)
    f= open(UPLOAD_FOLDER+flagDIR+"/flag.txt",'w')
    f.write('flag{test}')
    f.close()
    return UPLOAD_FOLDER+flagDIR+"/flag.txt"

def visit(url,lastViewed):
    flagFile=makeFlagFile()
    lastViewed.append(flagFile)
    with sync_playwright() as p:
        browser = p.chromium.launch(headless=False)
        context = browser.new_context(java_script_enabled=False)
        page = context.new_page()
        page.goto(url)
        time.sleep(10)
        #page.screenshot(path=f'/app/responses/response.png')
        page.screenshot(path=f'/home/navy356/CTFs/MyChalls/Chall1/responses/response.png')
        page.goto("file://"+flagFile)
        browser.close()
    
    return lastViewed