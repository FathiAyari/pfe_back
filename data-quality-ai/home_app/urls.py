from django.urls import path
from . import views

urlpatterns = [
    path('', views.index, name='index'),
    path('api/v1/login', views.login, name='login'),
]
