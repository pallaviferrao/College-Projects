from django import forms
from django.contrib.auth.models import User

from .models import Access, Query, Doctor


class AccessForm(forms.ModelForm):

    class Meta:
        model = Access
        fields = ['name', 'cancer_type', 'blood_group', 'email', 'cancer_logo']


class QueryForm(forms.ModelForm):

    class Meta:
        model = Query
        fields = ['query_title', 'solution']


class UserForm(forms.ModelForm):
    password = forms.CharField(widget=forms.PasswordInput)

    class Meta:
        model = User
        fields = ['username', 'email', 'password']

class DoctorForm(forms.ModelForm):

    class Meta:
        model = Doctor
        fields = ['doctor_name', 'suggestion']