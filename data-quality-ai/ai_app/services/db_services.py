import unidecode
from bson import ObjectId
from core.services.db_services import collection


def get_data_from_db(limit):
    pipeline = [
        {'$match': {'additional': {'$exists': False}}},
        {'$sample': {'size': limit}}
    ]
    return collection().aggregate(pipeline)


def update_db(data):
    for record in data:
        # get the record from the db
        product = collection().find_one({'_id': ObjectId(record['_id'])})
        # get the indexes of first MASS, VOLUME, DIMENSION, PIECE entity in record entities
        indexes = [
            next((i for i, obj in enumerate(record['entities']) if obj['label'] == 'MASS'), -1),
            next((i for i, obj in enumerate(record['entities']) if obj['label'] == 'VOLUME'), -1),
            next((i for i, obj in enumerate(record['entities']) if obj['label'] == 'DIMENSION'), -1),
            next((i for i, obj in enumerate(record['entities']) if obj['label'] == 'PIECE'), -1),
        ]
        entity = next((record['entities'][idx]['entity'] for idx in indexes if idx != -1), '')

        # if entity still empty and there is no MASS, VOLUME, DIMENSION, PIECE
        # get the first entity in record entities array
        if not entity and record['entities']:
            entity = record['entities'][0]['entity']

        if 'category' not in product.keys():
            product['category'] = ''

        keywords = __normalize_keywords(product['name'], entity)

        # update the record
        collection().find_one_and_update(
            {'_id': ObjectId(record['_id'])},
            {
                '$set': {
                    'keywords': keywords,
                    'additional': record['entities'],
                }
            }
        )


def __normalize_keywords(name, entity):
    key = f'{name} - {entity}'
    return unidecode.unidecode(key).lower()
