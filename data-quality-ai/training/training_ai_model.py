# Load Packages
from __future__ import unicode_literals, print_function

import time
import json
import spacy
import random
import shutil
from tqdm import tqdm
from pathlib import Path
from spacy.training import Example
from core import settings


def __load_model(model):
    if model is not None:
        nlp = spacy.load(model)  # load existing spaCy model
        print("Loaded model '%s'" % model)
    else:
        nlp = spacy.blank('fr')  # create blank Language class
        print("Created blank 'fr' model")
    return nlp


def __destroy_old_model(output_dir=Path('../model')):
    if output_dir is not None:
        output_dir = Path(output_dir)
        if output_dir.exists():
            shutil.rmtree(output_dir)
            print("Old Model got destroyed from path: ", output_dir)


def __add_labels(ner, train_data):
    for _, annotations in train_data:
        for ent in annotations['entities']:
            ner.add_label(ent[2])


def __training(nlp, train_data, n_iter):
    other_pipes = [pipe for pipe in nlp.pipe_names if pipe != 'ner']
    with nlp.disable_pipes(*other_pipes):  # only train NER
        optimizer = nlp.begin_training()
        for itn in range(n_iter):
            print(f'iteration number: {itn+1}/{n_iter}')
            random.shuffle(train_data)
            losses = {}
            for text, annotations in tqdm(train_data):
                doc = nlp.make_doc(text)
                # print(spacy.training.offsets_to_biluo_tags(doc, annotations['entities']))
                example = Example.from_dict(doc, annotations)
                try:
                    nlp.update(
                        [example],
                        drop=0.5,  # dropout - make it harder to memorise data
                        sgd=optimizer,  # callable to update weights
                        losses=losses)
                except Exception as e:
                    pass
            print(losses)


def __save_model(nlp, output_dir):
    if output_dir is not None:
        output_dir = Path(output_dir)
        if not output_dir.exists():
            output_dir.mkdir()
        nlp.to_disk(output_dir)
        print("Saved model to", output_dir)


def __test_trained_model(nlp, train_data):
    for text, _ in train_data:
        doc = nlp(text)
        print(text)
        print('Entities', [(ent.text, ent.label_) for ent in doc.ents])
        print('Tokens', [(t.text, t.ent_type_, t.ent_iob) for t in doc])
        print('-' * 50)


def update_train_data_file(data):
    with open(settings.TRAIN_DATA_FILE_PATH, 'r', encoding='utf8') as train_data_file:
        train_data = json.load(train_data_file)
        for record in data:
            train_data['annotations'].append([
                record['text'],
                {'entities': record['annotations']}
            ])
    with open(settings.TRAIN_DATA_FILE_PATH, 'w', encoding='utf8') as train_data_file:
        json.dump(train_data, train_data_file, ensure_ascii=False, indent=2)


def train(model=None, output_dir=Path('../model'), n_iter=50):
    with open(settings.TRAIN_DATA_FILE_PATH, 'r', encoding='utf8') as train_data_file:
        data = json.load(train_data_file)

        nlp = __load_model(model)

        """
            create the built-in pipeline components and add them to the pipeline
            nlp.create_pipe works for built-ins that are registered with spaCy
        """
        if 'ner' not in nlp.pipe_names:
            ner = nlp.add_pipe('ner', last=True)
        # otherwise, get it so we can add labels
        else:
            ner = nlp.get_pipe('ner')

        train_data = data['annotations']

        __add_labels(ner, train_data)

        """
            Add '-' and '+' as a tokens
        """
        infixes = nlp.Defaults.infixes + [r'([-+])']
        nlp.tokenizer.infix_finditer = spacy.util.compile_infix_regex(infixes).finditer

        """
            get names of other pipes to disable them during training
        """
        __training(nlp, train_data, n_iter)

        """
            test the trained model
        """
        # __test_trained_model(nlp, train_data)
        """
            Delete old trained model from disk
        """
        __destroy_old_model()
        """
            Save trained model to disk
        """
        __save_model(nlp, output_dir)


if __name__ == '__main__':
    start_time = time.time()
    train(n_iter=100)
    print(f'Execute Time : {time.time() - start_time}')
