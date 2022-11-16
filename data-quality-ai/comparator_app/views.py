import json

from django.http import JsonResponse, HttpResponse
from django.shortcuts import render
from django.views.decorators.csrf import csrf_exempt

# Create your views here.

from comparator_app.services import db_services, ai_services


def index(request):
    return render(request, 'comparator_app/index.html')


def compareProducts(request):
    token = request.GET.get('token')
    if token is None or token != request.session['token']:
        return HttpResponse(content='Invalid token!', status=403)

    products = db_services.get_data_from_db()
    comparedProducts, categories, brands = ai_services.compare(products)

    return JsonResponse(
        data={
            'products': comparedProducts,
            'categories': categories,
            'brands': brands
        },
        safe=False,
        status=200
    )


@csrf_exempt
def mergeProducts(request):
    body_unicode = request.body.decode('utf-8')
    body_data = json.loads(body_unicode)
    productsIds = body_data['products']

    token = body_data['token']
    if token is None or token != request.session['token']:
        return HttpResponse(content='Invalid token!', status=403)

    db_services.mergeSimilarProducts(productsIds)

    return HttpResponse(
        content='Products merged successfully!',
        status=200
    )
