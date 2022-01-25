import puppeteer from 'puppeteer';

const browserOptions = {
    headless: true,
    args: [
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
        '--no-sandbox',
        '--safebrowsing-disable-auto-update'
    ]
};

async function emergencyAlert() {
    const browser = await puppeteer.launch(browserOptions);
    const page = await browser.newPage();

    await page.setCookie({
        'name': 'flag',
        'value': 'flag{test_flag_goes_here}',
        'url': 'http://localhost:8888'
    });

    const url = 'http://localhost:8888/handle-emergency';
    const pageOptions = { waitUntil: 'networkidle2' };

    await page.goto(url, pageOptions);

    await browser.close();
};

export { emergencyAlert };