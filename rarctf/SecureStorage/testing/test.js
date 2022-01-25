const puppeteer = require("puppeteer");
const path = require("path");

let ext = path.resolve(__dirname, "./extension/");

let queue = [];
const addToQueue = (url) => queue.push(url);

const TIMEOUT = 4000;
const DELAY = 500;

const visit = (url) => {
    let page, browser;
    return new Promise(async (resolve, reject) => {
        try {
            browser = await puppeteer.launch({
                headless: false,
                args: [
                    '--no-sandbox',
                    '--disable-setuid-sandbox',
                    '--disable-dev-shm-usage'
                ],
                dumpio: true,
                executablePath: '/usr/bin/google-chrome-stable'
            });
            page = await browser.newPage();

            /* load flag */
            await page.goto('http://localhost:4000', {
                waitUntil: "networkidle2"
            });
            await page.evaluate(flag => {
                localStorage.message = flag;
            }, 'flag{TEST}');

            await page.goto(url, {
                waitUntil: "networkidle2"
            });
            await new Promise(resolve => setTimeout(resolve, 10000));
            await page.waitForTimeout(TIMEOUT);
            await page.close();
            page = null;
        } catch (err) {
            console.log(err);
        } finally {
            if (page) await page.close();
            if (browser) await browser.close();
            resolve();
        }
    });
};

const loop = async () => {
    while (true) {
        let url = queue.shift();
        if (url) {
            console.log("vistiting:", url, queue);
            await visit(url);
        }
        await new Promise((resolve, reject) => setTimeout(resolve, DELAY));
    }
};

loop();
module.exports = {
    addToQueue
};

try {
    url='https://64898b4f8b7b.ngrok.io/exploit.html';
    let check = new URL(url);
    if(check.protocol !== "http:" && check.protocol !== "https:")
        throw new Error("nope");
    addToQueue(url);
}
catch(err) {
    console.log(`Invalid URL`);
}