const puppeteer = require('puppeteer');
const parse = require('url-parse');

const cookies = [{
    name: 'jsession',
    value: 'DELETE',
    domain: "127.0.0.1",
    httpOnly:true
}];

const bot = async function (url){
    const URL = parse(url, true)
    try{
        const browser = await puppeteer.launch({
            headless: true,
            args: [
                '--no-sandbox',
                '--disable-setuid-sandbox',
                '--disable-dev-shm-usage'
            ],
            dumpio: true,
        });
        page = await browser.newPage('http://127.0.0.1:80');
        await page.setCookie(...cookies);

        console.log(URL.href)
        await page.goto(`http://127.0.0.1:80/?blog=${URL.href}`);
        setTimeout(() => {
            browser.close();
        }, 4000);
    } catch (err) {
        console.log(`err : ${err}`);
    }
}

module.exports = bot;