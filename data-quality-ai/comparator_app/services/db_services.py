from bson import ObjectId
from unidecode import unidecode
from collections import defaultdict

from core.services.db_services import collection


def get_data_from_db():
    pipeline = [
        {'$match': {
            'keywords': {'$exists': True}
        }},
    ]
    data = collection().aggregate(pipeline)
    return __groupByCategoryAndBrand(data)


def mergeSimilarProducts(ids):
    objIds = [ObjectId(productId) for productId in ids]

    products = collection().find({'_id': {'$in': objIds}})
    toMergeInto = next((product for product in products.clone() if 'barCode' in product.keys()), None)

    if toMergeInto is None:
        toMergeInto = products.next()

    other_products = [product for product in products if product['_id'] != toMergeInto['_id']]

    for product in other_products:
        toMergeInto['offers'].extend(product['offers'])
        collection().delete_one(product)

    collection().update_one(
        {'_id': toMergeInto['_id']},
        {'$set': {'offers': toMergeInto['offers']}}
    )


def __groupByCategoryAndBrand(data):
    byCategory = defaultdict(list)
    for product in data:
        if 'category' not in product.keys():
            product['category'] = ''
        if 'brand' not in product.keys():
            product['brand'] = ''
        if 'barCode' not in product.keys():
            product['barCode'] = None
        byCategory[__normalize(product['category'])].append({
            'id': str(product['_id']),
            'name': product['name'].lower(),
            'description': product['description'].lower(),
            'keywords': product['keywords'],
            'image': product['image'],
            'barCode': product['barCode'],
            'brand': product['brand'],
            'storeId': [offer['storeId'] for offer in product['offers']]
        })

    cleanData = defaultdict(dict)
    for category in byCategory:
        byBrand = defaultdict(list)
        for product in byCategory[category]:
            byBrand[__normalize(product['brand'])].append({
                'id': product['id'],
                'name': product['name'],
                'description': product['description'],
                'keywords': product['keywords'],
                'image': product['image'],
                'barCode': product['barCode'],
                'storeId': product['storeId']
            })
        cleanData[category] = byBrand
    return __filterProducts(cleanData)


def __normalize(string):
    return unidecode(string.replace(' ', '_').lower())


def __filterProducts(products):
    # return only products within the categories and brands that have more than one offer
    data = defaultdict(dict)
    for category in products.keys():
        for brand in products[category].keys():
            if len(products[category][brand]) > 1:
                data[category][brand] = products[category][brand]
    return data
