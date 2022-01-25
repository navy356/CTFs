const puppeteer = require('puppeteer');
const express = require("express");
const app = express();

const browser_options = {
    headless: false,
    args: [
        '--no-sandbox',
        '--disable-background-networking',
        '--disable-default-apps',
        '--disable-extensions',
        '--disable-gpu',
        '--disable-sync',
        '--disable-translate',
        '--hide-scrollbars',
        '--metrics-recording-only',
        '--mute-audio',
        '--no-first-run',
        '--safebrowsing-disable-auto-update',
    ],
};

function delay(timeout) {
    return new Promise((resolve) => {
        setTimeout(resolve, timeout);
    });
}

async function visit(website) {
    const browser = await puppeteer.launch(browser_options);
    const page = await browser.newPage();
    await page.goto(website, {
        waitUntil: 'load',
        timeout: 0

    });
    await delay(30000);
    await browser.close();
}

app.get("/", (req, res) => {
    var website = req.query.url;
    visit(website).then((e) => { res.send("Visited successfully!") }).catch((err) => { res.send("Error") });
})
app.listen(8000, () => { console.log("app listening") })


visit("http://cbd5-103-93-37-7.ngrok.io/test.html")
    //visit("http://172.16.0.4:5000/home?feedback=%3Ca+id%3DSETTINGS+data-location%3Deval%28location.hash.slice%281%29%29+data-timezone%3Dtest%3E%3Cp+id%3DshowInfos%3E%3Ca+id%3DSETTINGS+name%3Dcheck%3E&submit=Add+Feedback")