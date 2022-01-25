from django.db import models
# from django.contrib.auth.models import User
from django.contrib.auth.models import AbstractUser

class DreamUser(AbstractUser):
    signpath = models.TextField()

class Document(models.Model):
    user = models.ForeignKey(DreamUser, on_delete=models.CASCADE)
    time = models.DateTimeField(auto_now_add=True)
    title = models.CharField(max_length=50)
    contents = models.TextField()

class SignDocument(models.Model):
    user = models.ForeignKey(DreamUser, on_delete=models.CASCADE)
    document = models.ForeignKey(Document, on_delete=models.CASCADE)
    time = models.DateTimeField(auto_now_add=True)
    pdf = models.BinaryField()

class Sign(models.Model):
    user = models.ForeignKey(DreamUser, on_delete=models.CASCADE)
    time = models.DateTimeField(auto_now_add=True)
    image = models.BinaryField()
