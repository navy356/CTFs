from django import forms
from .models import Document, DreamUser

class UserCreationForm(forms.ModelForm):
    password1 = forms.CharField(label='Password', widget=forms.PasswordInput)
    password2 = forms.CharField(
        label='Password confirmation', widget=forms.PasswordInput)

    class Meta:
        model = DreamUser
        fields = ('username',)

    def clean_password2(self):
        password1 = self.cleaned_data.get("password1")
        password2 = self.cleaned_data.get("password2")
        if password1 and password2 and password1 != password2:
            raise forms.ValidationError("Passwords don't match")
        return password2

    def save(self, commit=True):
        user = super().save(commit=False)
        user.set_password(self.cleaned_data["password1"])
        if commit:
            user.save()
        return user

class DocumentForm(forms.ModelForm):
    class Meta:
        model = Document
        fields = ['title', 'contents']


class SignForm(forms.Form):
    signpath = forms.CharField()

    def validate(self, *args, **kwargs):
        super().validate(*args, **kwargs)
        signpath = self.cleaned_data.get('')
        if not signpath or len(signpath) < 2 or signpath[0] != '/':
            raise forms.ValidationError('')
