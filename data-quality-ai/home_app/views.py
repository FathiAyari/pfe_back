from secrets import token_hex

from django.shortcuts import render
from django.http import JsonResponse
from django.views.decorators.csrf import csrf_exempt

import json
from decouple import config


def index(request):
    return render(request, 'home_app/index.html')


@csrf_exempt
def login(request):
    env_username = config('DJANGO_ADMIN_USERNAME')
    env_password = config('DJANGO_ADMIN_PASSWORD')

    body_unicode = request.body.decode('utf-8')
    body_data = json.loads(body_unicode)
    request_username = body_data['username']
    request_password = body_data['password']

    if request_username != env_username or request_password != env_password:
        return JsonResponse(status=401, data={'message': 'Login Failed'})

    if 'token' in request.session:
        del request.session['token']

    request.session['token'] = token_hex(16)

    return JsonResponse(status=200, data={'message': 'Login Success', 'token': request.session['token']})
