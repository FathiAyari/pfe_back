from pymongo import MongoClient
from core.settings import MONGO_DB_HOST, MONGO_DB_PORT, MONGO_DB_NAME, MONGO_DB_USER, MONGO_DB_PASSWORD


def collection():
    client = MongoClient(f'mongodb://{MONGO_DB_USER}:{MONGO_DB_PASSWORD}@{MONGO_DB_HOST}:{MONGO_DB_PORT}')
    db = client[MONGO_DB_NAME]
    return db.get_collection('Products')
