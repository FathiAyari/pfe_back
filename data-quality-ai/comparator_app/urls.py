from django.urls import path
from . import views

urlpatterns = [
    path('', views.index, name='index'),
    path('api/v1/compare', views.compareProducts, name='compare_products'),
    path('api/v1/merge', views.mergeProducts, name='merge_products'),
]
