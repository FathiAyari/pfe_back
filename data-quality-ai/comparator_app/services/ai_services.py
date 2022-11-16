import spacy
from collections import defaultdict


def compare(data):
    fr_model = spacy.load('fr_core_news_md')
    productsWithSimilar = defaultdict(list)
    for category in data:
        productsByBrand = defaultdict(list)
        for brand in data[category]:
            products = data[category][brand]
            productsByBrand[brand] = __getSimilarity(products, fr_model)
        productsWithSimilar[category].append(productsByBrand)
    return __filterProductsWithSimilar(productsWithSimilar)


def __getSimilarity(products, fr_model):
    compared = []
    for product in products:
        ref_sentence = fr_model(product['keywords'])

        all_docs = [
            (info, fr_model(info['keywords']))
            for info in products
            if info['id'] != product['id'] and __storeIdNotExists(info['storeId'], product['storeId'])
        ]

        similar = []
        for doc in all_docs:
            if {product['id'], doc[0]['id']} not in compared:
                compared.append({product['id'], doc[0]['id']})
                similar.append({
                    'similarity': doc[1].similarity(ref_sentence),
                    'product': {
                        'id': doc[0]['id'],
                        'name': doc[0]['name'],
                        'description': doc[0]['description'],
                        'keywords': doc[0]['keywords'],
                        'image': doc[0]['image'],
                        'storeId': doc[0]['storeId'],
                        'barCode': doc[0]['barCode'],
                    }
                })

        sorted_similar = sorted(similar, key=lambda k: k['similarity'], reverse=True)
        product['similar'] = sorted_similar
    return __getProductsWithSimilar(products)


def __storeIdNotExists(storeId1, storeId2):
    # len == 0: there is no common storeId: return True
    # len != 0: there is common storeId: return False
    return not set(storeId1).intersection(storeId2)


def __getProductsWithSimilar(products):
    return [product for product in products if len(product['similar']) > 0]


def __filterProductsWithSimilar(products):
    all_brands = []
    categories = []
    for category in products:
        # loop through the products of the category and get the brands if it's not empty
        if categoryBrands := [brand for brand in products[category][0] if products[category][0][brand]]:
            categories.append(category)
            all_brands.append(categoryBrands)

    data = defaultdict(dict)
    for categoryIndex, category in enumerate(categories):
        for brand in all_brands[categoryIndex]:
            data[category][brand] = list(products[category][0][brand])

    return data, categories, all_brands
