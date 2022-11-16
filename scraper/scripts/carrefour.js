const axios = require("axios");
const https = require('https');
const fs = require("fs");
const cheerio = require('cheerio')

const httpsAgent = new https.Agent({rejectUnauthorized: false});
const client = axios.create({httpsAgent})

const base_url = 'https://wvv7p3w4la-dsn.algolia.net/1/indexes/*/queries'
const desc_url = 'https://www.carrefour.tn/default/products_list/index/getproductsinfo'
const menu_url = 'https://www.carrefour.tn/default/carrefourtopmenu/menu/menu'

const loadCategories = async () => {
    let response = null
    try {
        response = await client({
            method: 'get',
            url: menu_url,
            data: {
                "outermostClass": "level-top",
                "childrenWrapClass": "submenu"
            }
        })
    } catch (e) {
        console.log(e)
    }
    if (response == null) {
        throw new Error('Error while loading categories')
    }
    const $ = cheerio.load(response.data['menuHtml'])
    const categories = []
    $('#inner-0 .cat-inner-container .cat-card').each(
        (i, el) => {
            const category = $(el).find('p').text().trim()
            categories.push(category)
        }
    )
    return categories
}

const save = (file, data) => {
    fs.writeFile(file, JSON.stringify(data, null, 2), (err) => {
        if (err) {
            console.error(err)
            return
        }
        console.log("Successfully written data to file")
    })
}

const getData = async (index, category) => {
    let response = null
    try {
        response = await client({
            method: 'POST',
            url: base_url,
            headers: {
                'Accept': 'application/json',
                'x-algolia-agent': 'Algolia%20for%20JavaScript%20(3.35.1)%3B%20Browser%3B%20instantsearch.js%20(4.15.0)%3B%20Magento2%20integration%20(3.2.0)%3B%20JS%20Helper%20(3.4.4)',
                'x-algolia-application-id': 'WVV7P3W4LA',
                'x-algolia-api-key': 'Njc4OTZkY2IxZmRjYWRiNmM1ZjViOTI4NzQ0NmVmZTNhNWM0NTNmNWZlYzY0Y2FiNTc2ZmMxYmZhZWVhNjUwOXRhZ0ZpbHRlcnM9'
            },
            data: {
                "requests": [
                    {
                        "indexName": "magento2_default_products",
                        "params": `page=${index}&facetFilters=["categories.level0:${category}"]`
                    }
                ]
            }
        })
    } catch (err) {
        console.error(err)
    }

    return response
}

const getDataWithRetry = async (index, category, maxRetry) => {
    let response = null
    let attempt = -1
    do {
        attempt++
        response = await getData(index, category)
    } while (response == null && attempt <= maxRetry)

    return response
}

const loadData = async (index, category) => {
    const response = await getDataWithRetry(index, category, 3)
    if (response == null) {
        throw `Cannot fetch data from page ${index}`
    }
    console.log('Data fetched!')
    return response.data['results'][0]
}

const extractProductsIds = (products) => {
    let ids
    products.map(
        (product, index) => {
            if (index === 0) ids += `ids[]=${product['objectID']}`
            else ids += `&ids[]=${product['objectID']}`
        }
    )
    return ids
}

const getDescriptions = async (productsIds) => {
    let response = []
    let attempt = -1

    do {
        attempt++
        try {
            response = await client({
                method: 'POST',
                url: desc_url,
                headers: {
                    'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8',
                    'X-Requested-With': 'XMLHttpRequest'
                },
                data: `${productsIds}`
            })
        } catch (err) {
            console.error(err)
        }
    } while (response == null && attempt <= 3)

    if (response == null) {
        throw 'Cannot fetch data'
    }

    console.log('Data Description fetched!')
    return response.data && Object.keys(response.data).includes('products') ?
        response.data['products'].map(product => {
            const result = {
                'id': product['id'],
                'description': ''
            }

            if (product['short_description']) {
                result['description'] = cheerio.load(product['short_description']).text().trim()
                return result
            }

            return result
        })
        : []
}

const scrapeData = async (file, category) => {
    try {
        if (fs.existsSync(file)) {
            console.log('File already exists')
            return
        }
        const today = new Date().toISOString();
        const results = []
        let index = 0
        let pageCount

        do {
            const data = await loadData(index, category)
            if (index === 0) pageCount = data['nbPages']
            const productsIds = extractProductsIds(data['hits'])
            const descriptions = await getDescriptions(productsIds)

            data['hits'].map(
                hit => {
                    let description = descriptions.find(
                        desc => desc['id'] === hit['objectID'])
                    results.push({
                        'name': hit['name'].trim(),
                        'externalId': hit['objectID'],
                        'barcode': hit['sku'],
                        'price': hit['price']['TND']['default'],
                        'image': hit['image_url'],
                        'brand': hit['marque'] ? hit['marque'] : '',
                        'description': description ? description['description'] : '',
                        'link': hit['url'],
                        'category': category,
                        'storeId': 1,
                        'creationDate': today
                    })
                }
            )
        } while (++index < pageCount)

        save(file, results)

    } catch (err) {
        console.log(err)
    }
}

(async () => {
    console.time('Scraping Time')
    const promises = []
    const path = process.argv.splice(2)[0]
    const categories = await loadCategories()

    for (let category of categories) {
        console.log(`Start scraping of category ${category}`)
        const categoryName = category.split(' ').join('-').toLowerCase()
        const file = `carrefour_${categoryName}_${new Date().toISOString().match("(.*)T")[1]}.json`
        let promise = scrapeData(path + file, category.replace('&', '%26'))
        promises.push(promise)
    }
    Promise.all(promises).then(() => console.timeEnd('Scraping Time'))
})()
