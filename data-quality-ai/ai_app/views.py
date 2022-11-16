from django.http import HttpResponse, JsonResponse
from django.shortcuts import render

import os
import json

from django.views.decorators.csrf import csrf_exempt

from ai_app.services import db_services, ai_services
from core.settings import AI_MODEL_PATH


def index(request):
    return render(request, 'ai_app/index.html')


def extract_entities(request):
    limit = int(request.GET.get('nbrRecords', 12))
    token = request.GET.get('token', None)

    if token is None or token != request.session['token']:
        return HttpResponse(content='Invalid token!', status=403)

    data = db_services.get_data_from_db(limit)

    if os.path.exists(AI_MODEL_PATH):
        results = ai_services.extract_data(data)

        return JsonResponse(
            results,
            safe=False,
            status=200
        )
    else:
        return HttpResponse(status=404)


@csrf_exempt
def update(request):
    body_unicode = request.body.decode('utf-8')
    body_data = json.loads(body_unicode)
    data = body_data['data']
    token = body_data['token']

    if token is None or token != request.session['token']:
        return HttpResponse(content='Invalid token!', status=403)

    ai_services.update_model(data)
    db_services.update_db(data)

    return HttpResponse(content='Model is updating...', status=200)
