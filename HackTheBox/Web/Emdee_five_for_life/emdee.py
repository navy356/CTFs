from selenium import webdriver
import sys
from selenium.webdriver.chrome.options import Options
from selenium.webdriver.common.keys import Keys
import hashlib
from time import sleep
 

TARGET_URL = "http://138.68.178.56:31734/"


def get_emdee_5():
    chrome_driver = webdriver.Chrome()
    
    chrome_driver.get(TARGET_URL)
 
    str=chrome_driver.find_element_by_xpath("/html/body/h3").text.encode("utf-8")

    m=hashlib.md5()
    m.update(str)
    
    hash=m.hexdigest()

    hash_input = chrome_driver.find_element_by_name("hash")
    hash_input.send_keys(hash)

    submit = chrome_driver.find_element_by_xpath("//input[@type='submit']")
    submit.click()

    flag=chrome_driver.find_element_by_xpath("//p").text
    print(flag)

    chrome_driver.close()

get_emdee_5()