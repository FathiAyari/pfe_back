from django.urls import path
from . import views

urlpatterns = [
    path('', views.index, name='index'),
    path('api/v1/extractEntities/', views.extract_entities, name='extract_entities'),
    path('api/v1/update/', views.update, name='update_ai_model_&_db'),
]
