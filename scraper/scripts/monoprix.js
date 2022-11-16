const axios = require("axios");
const https = require('https');
const cheerio = require("cheerio");
const fs = require("fs");

const httpsAgent = new https.Agent({ rejectUnauthorized: false });
const client = axios.create({ httpsAgent })

const categories = [
    'promotions',
    '1030-maison-et-exterieur',
    '320-le-marche',
    '3-epicerie-salee',
    '13-boissons',
    '12-epicerie-sucree',
    '14-cremerie-et-surgele',
    '15-hygiene-beaute',
    '16-produits-menagers',
    '357-animalerie'
]

const getPageCount = ($) => {
    const pagination = $('.pagination .js-search-link')
    if (pagination.length > 2) {
        return $(pagination[pagination.length - 2]).text().trim()
    }
    return 1
}

const getPage = async (currentPageUrl) => {
    let response = null
    try {
        response = await client.get(currentPageUrl, {
            headers: { 'Accept': 'text/html' }
        })
    } catch (err) {
        console.error(err)
    }

    return response
}

const getPageWithRetry = async (currentPageUrl, maxRetry) => {
    let response = null
    let attempt = -1
    do {
        attempt++
        response = await getPage(currentPageUrl)
    } while (response == null && attempt <= maxRetry)

    return response
}

const loadPage = async (url, page) => {
    const currentPageUrl = `${url}?page=${page}`
    console.log(currentPageUrl)

    const response = await getPageWithRetry(currentPageUrl, 3)
    if (response == null) {
        throw `Cannot download page ${currentPageUrl}`
    }
    console.log('Page downloaded')
    return cheerio.load(response.data)
}

const scrapeData = async (file, url, category) => {
    try {
        if (fs.existsSync(file)) {
            console.log('File already exists')
            return
        }
        const today = new Date().toISOString();
        const results = []
        let index = 1
        let pageCount = 1

        do {
            let $ = await loadPage(url, index)
            if (index === 1) {
                pageCount = getPageCount($)
                console.log(`${pageCount} page will be scraped...`)
            }

            let products = $('.product-miniature')
            if (category.split('-').length > 1) category = category.split('-').slice(1).join(' ')
            products.each(
                (i, product) => {
                    results.push({
                        'externalId': $(product).attr('data-id-product'),
                        'name': $(product).find('.product-title a').text(),
                        'price': $(product).find('.price').text(),
                        'image': $(product).find('.thumbnail img').attr('src'),
                        'brand': $(product).find('.div_manufacturer_name').text(),
                        'description': $(product).find('.div_contenance').text().trim(),
                        'link': $(product).find('.thumbnail').attr('href'),
                        'category': category,
                        'storeId': 3,
                        'creationDate' : today
                    })
                }
            )
        } while (++index <= pageCount)

        fs.writeFile(file, JSON.stringify(results, null, 2), (err) => {
            if (err) {
                console.error(err);
                return;
            }
            console.log("Successfully written data to file");
        })

    } catch (err) {
        console.log(err)
    }
}

(async () => {
    console.time('Scraping Time')
    const promises = []
    const path = process.argv.splice(2)[0]
  for (let category of categories) {
      console.log(`Start scraping of category ${category}`)
      const url = `https://courses.monoprix.tn/ainZaghouan/${category}`
      const file = `monoprix_${category}_${new Date().toISOString().match("(.*)T")[1]}.json`
      let promise = scrapeData(path + file, url, category)
      promises.push(promise)
    }
    Promise.all(promises).then(() => console.timeEnd('Scraping Time'))
})()