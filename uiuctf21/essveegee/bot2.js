const { chromium } = require('playwright-chromium');

(async () => {
    const svgPath = '/home/navy356/CTFs/uiuctf21/essveegee/demo.svg'
    const outPath = '/home/navy356/CTFs/uiuctf21/essveegee/out.svg'
    
    console.log('beginning conversion')
    const browser = await chromium.launch({ headless: false });
    const context = await browser.newContext({
        javaScriptEnabled: false
    })
    context.setDefaultTimeout(10000)

    const svgfile = svgPath.split('/').map((pathPart) => encodeURI(pathPart)).join('/')
    const page = await context.newPage()
    await page.goto('file://' + svgfile)
    const renderSettings = {
        path: outPath,
        type: 'png',
        omitBackground: true,
        fullPage: true
    }
    await page.waitForTimeout(1200000)
    const svgEl = await page.waitForSelector('svg')
    await svgEl.screenshot(renderSettings)
    await browser.close()
})()