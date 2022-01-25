from django.urls import path
from django.contrib.auth import views as auth_views

from . import views

app_name = 'sign_service'

urlpatterns = [
    path('', views.index, name='index'),

    path('logout/', auth_views.LogoutView.as_view(), name='logout'),
    path('login/', auth_views.LoginView.as_view(), name='login'),
    path('signup/', views.signup, name='signup'),

    path('sign/<int:sign_id>', views.sign_view, name='sign_view'),
    path('mysign/', views.mysign, name='mysign'),
    path('mysign/upload', views.mysign_upload, name='mysign_upload'),

    path('signdocument/<int:document_id>', views.sign_document_view, name='sign_document_view'),

    path('document/', views.documents, name='documents'),
    path('document/create', views.document_create, name='document_create'),
    path('document/<int:document_id>', views.document_view, name='document_view'),
    path('document/<int:document_id>/render', views.document_render, name='document_render'),
    path('document/<int:document_id>/render_internal/<int:user_id>', views.document_render_internal, name='document_render_internal'),
]