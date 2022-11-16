const axios = require("axios");
const https = require('https');
const cheerio = require("cheerio");
const fs = require("fs");

const httpsAgent = new https.Agent({rejectUnauthorized: false});
const client = axios.create({httpsAgent})

const categories = [
    '332-promotions',
    '10-boissons',
    '31-epicerie',
    '130-cremerie-et-surgele',
    '160-le-marche',
    '268-produits-menagers',
    '210-hygiene-et-beaute',
    '322-univers-bebes',
    '821-maison',
    '874-mode-loisirs-et-sport',
    '312-animalerie',
    '854-auto-bricolage-et-plein-air',
];

function getPageCount($) {
    const list = $(".pagination .js-search-link");
    if (list.length > 2) {
        return $(list[list.length - 2]).text().trim();
    }
    return 1;
}

async function getPage(currentPageUrl) {
    let response = null;
    try {
        response = await client.get(currentPageUrl, {
            headers: {"Accept": "text/html"},
        });
    } catch (e) {
        console.error(e);
    }
    return response;
}

async function getPageWithRetry(currentPageUrl, maxRetry) {
    let response = null;
    let attempt = -1;
    do {
        attempt++;
        response = await getPage(currentPageUrl);
    } while (response == null && attempt <= maxRetry)

    return response;
}

async function loadPage(url, page) {
    const currentPageUrl = `${url}?page=${page}`;
    console.log(currentPageUrl);
    let response ;
    response = await getPageWithRetry(currentPageUrl,3);
    if (response == null) {
        throw "Cannot download page " + currentPageUrl ;
    }
    console.log('page downloaded')
    return cheerio.load(response.data);
}

async function main() {
    console.time('Scraping Time');
    const promises = [];
    for (let category of categories) {
        console.log(`Start scraping of category ${category}`);
        const url = `https://www.geantdrive.tn/azur-city/${category}`;
        const file = `geant_${category}_${new Date().toISOString().match("(.*)T")[1]}.json`;
        const myArgs = process.argv.slice(2);
        const path = myArgs[0];
        let promise = scrapeData(path + file, url, category);
        promises.push(promise);
    }
    Promise.all(promises).then(
        ()=>console.timeEnd('Scraping Time')
    );
}

async function scrapeData(file, url, category) {
    try {
        if (fs.existsSync(file)) {
            console.log('File already exists');
            return;
        }
        const today = new Date().toISOString();
        const results = [];
        let i = 1;
        let pageCount=1;
        do {
            let $ = await loadPage(url, i);
            if (i === 1) {
                pageCount = getPageCount($);
                console.log(pageCount + ' pages will be scraped ...');
            }
            let products = $('.product-miniature');
            if (category.split('-').length > 1) category = category.split('-').slice(1).join(' ')
            products.each(
                (i, p) => {
                    const $p = $(p);
                    return results.push({
                        'name': $p.find(".product-title").attr("title"),
                        'price': $p.find(".price").text(),
                        'image': $p.find("img").attr("src"),
                        'brand': $p.find(".manufacturer_product").text(),
                        'description': $p.find(".product_short").text(),
                        'link': $p.find(".product-title a").attr("href"),
                        'externalId': $p.attr("data-id-product"),
                        'category': category,
                        'storeId': 2,
                        'creationDate' : today
                    });
                }
            );
        } while (++i <= pageCount)
       // console.dir(results);

        fs.writeFile(file, JSON.stringify(results, null, 2), (err) => {
            if (err) {
                console.error(err);
                return;
            }
            console.log("Successfully written data to file");
        });
    } catch (err) {
        console.error(err);
    }

}

main();