const axios = require('axios')
const cheerio = require('cheerio')
const fs = require('fs')
process.env.NODE_TLS_REJECT_UNAUTHORIZED = '0'

function main() {
    const url = `https://aziza.tn/fr/home`
    const today = new Date().toISOString();
    let file = `aziza_${today.match('(.*)T')[1]}.json`
    const path = process.argv.splice(2)[0]
    if (fs.existsSync(file)) {
        console.log('File already exists')
        return
    }

    axios.get(url)
        .then(response => {
            const $ = cheerio.load(response.data)
            const result = []

            $('#list_products-aN li').each((i, element) => {
                console.log(`start scrapping product ${i}`)
                const $p = $(element)

                result.push({
                    'name': $p.find('.product-item-link').text().trim(),
                    'price': $p.find('.price').text(),
                    'image': $p.find('.product-image-photo').attr('src'),
                    'brand': $p.find('.brand').text().trim(),
                    'description': $p.find('.more_title').text().trim(),
                    'link': $p.find('.actions-primary span>a').attr('href'),
                    'externalId': $p.find('.price-box').attr('data-product-id'),
                    'storeId': 0,
                    'creationDate' : today
                })
            })

            fs.writeFileSync(path + file, JSON.stringify(result, null, 2))
            console.log('Successfully written data to file')
        })
        .catch(error => console.log(error))
}

main()