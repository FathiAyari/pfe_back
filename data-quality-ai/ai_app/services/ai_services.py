import spacy
from pathlib import Path
from core import settings
from training.training_ai_model import train, update_train_data_file


def extract_data(data):
    ai_model = spacy.load(settings.AI_MODEL_PATH)
    results = []
    for row in data:
        entities = []
        annotations = []
        text = f'{row["name"]} {row["description"]}'
        for ent in ai_model(text).ents:
            entities.append({
                'entity': ent.text,
                'label': ent.label_
            })
            annotations.append([
                ent.start_char,
                ent.end_char,
                ent.label_
            ])
        results.append({
            '_id': str(row['_id']),
            'text': text,
            'entities': entities,
            'annotations': annotations,
            'image': row['image'],
            'isAccepted': False
        })

    return results


def update_model(data):
    update_train_data_file(data)
    train(output_dir=Path(settings.AI_MODEL_PATH))
