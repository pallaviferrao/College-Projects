from django.contrib.auth import authenticate, login
from django.contrib.auth import logout
from django.http import JsonResponse
from django.shortcuts import render, get_object_or_404
from django.db.models import Q
from os import access

from django.template import RequestContext

from .forms import AccessForm, QueryForm, UserForm, DoctorForm
from .models import Access, Query

AUDIO_FILE_TYPES = ['wav', 'mp3', 'ogg']
IMAGE_FILE_TYPES = ['png', 'jpg', 'jpeg']


def create_access(request):
    if not request.user.is_authenticated():
        return render(request, 'cancer/login.html')
    else:
        form = AccessForm(request.POST or None, request.FILES or None)
        if form.is_valid():
            access = form.save(commit=False)
            access.user = request.user
            access.cancer_logo = request.FILES['cancer_logo']
            file_type = access.cancer_logo.url.split('.')[-1]
            file_type = file_type.lower()
            if file_type not in IMAGE_FILE_TYPES:
                context = {
                    'access': access,
                    'form': form,
                    'error_message': 'Image file must be PNG, JPG, or JPEG',
                }
                return render(request, 'cancer/create_access.html', context)
            access.save()
            return render(request, 'cancer/detail.html', {'access': access})
        context = {
            "form": form,
        }
        return render(request, 'cancer/create_access.html', context)


def create_query(request, access_id):
    form = QueryForm(request.POST or None, request.FILES or None)
    access = get_object_or_404(Access, pk=access_id)
    if form.is_valid():
        access_querys = access.query_set.all()
        for s in access_querys:
            if s.query_title == form.cleaned_data.get("query_title"):
                context = {
                    'access': access,
                    'form': form,
                    'error_message': 'You already added that query',
                }
                return render(request, 'cancer/create_query.html', context)
        query = form.save(commit=False)
        query.access = access
        query.save()
        return render(request, 'cancer/detail.html', {'access': access})
    context = {
        'access': access,
        'form': form,
    }
    return render(request, 'cancer/create_query.html', context)


def delete_access(request, access_id):
    access = Access.objects.get(pk=access_id)
    access.delete()
    access = Access.objects.filter(user=request.user)
    return render(request, 'cancer/index.html', {'access': access})


def delete_query(request, access_id, query_id):
    access = get_object_or_404(Access, pk=access_id)
    query = Query.objects.get(pk=query_id)
    query.delete()
    return render(request, 'cancer/detail.html', {'access': access})


def detail(request, access_id):
    if not request.user.is_authenticated():
        return render(request, 'cancer/login.html')
    else:
        user = request.user
        access = get_object_or_404(Access, pk=access_id)
        return render(request, 'cancer/detail.html', {'access': access, 'user': user})


def index(request):
    if not request.user.is_authenticated():
        return render(request, 'cancer/login.html')
    else:
        access = Access.objects.filter(user=request.user)
        query_results = Query.objects.all()
        querys = request.GET.get("q")
        if querys:
            access = access.filter(
                Q(access_title__icontains=querys) |
                Q(blood_group__icontains=querys)
            ).distinct()
            query_results = query_results.filter(
                Q(query_title__icontains=querys)
            ).distinct()
            return render(request, 'cancer/index.html', {
                'access': access,
                'query': query_results,
            })
        else:
            return render(request, 'cancer/index.html', {'access': access})

def home(request):

            return render(request, 'cancer/home.html')



def logout_user(request):
    logout(request)
    form = UserForm(request.POST or None)
    context = {
        "form": form,
    }
    return render(request, 'cancer/login.html', context)


def login_user(request):
    if request.method == "POST":
        username = request.POST['username']
        password = request.POST['password']
        user = authenticate(username=username, password=password)
        if user is not None:
            if user.is_active:
                login(request, user)
                access = Access.objects.filter(user=request.user)
                return render(request, 'cancer/index.html', {'access': access})
            else:
                return render(request, 'cancer/login.html', {'error_message': 'Your account has been disabled'})
        else:
            return render(request, 'cancer/login.html', {'error_message': 'Invalid login'})
    return render(request, 'cancer/login.html')


def register(request):
    form = UserForm(request.POST or None)
    if form.is_valid():
        user = form.save(commit=False)
        username = form.cleaned_data['username']
        password = form.cleaned_data['password']
        user.set_password(password)
        user.save()
        user = authenticate(username=username, password=password)
        if user is not None:
            if user.is_active:
                login(request, user)
                access = Access.objects.filter(user=request.user)
                return render(request, 'cancer/index.html', {'access': access})
    context = {
        "form": form,
    }
    return render(request, 'cancer/register.html', context)


def query(request, filter_by):
    if not request.user.is_authenticated():
        return render(request, 'cancer/login.html')
    else:
        try:
            query_ids = []
            for access in Access.objects.filter(user=request.user):
                for query in access.query_set.all():
                    query_ids.append(query.pk)
            users_query = Query.objects.filter(pk__in=query_ids)
            if filter_by == 'favourites':
                users_query = users_query.filter(is_favorite=True)
        except Access.DoesNotExist:
            users_query = []
        return render(request, 'cancer/query.html', {
            'query_list': users_query,
            'filter_by': filter_by,
        })


def add_suggestion(request, access_id, query_id):
    form = DoctorForm(request.POST or None, request.FILES or None)
    access = get_object_or_404(Access, pk=access_id)
    query = get_object_or_404(Query, pk=query_id)

    if form.is_valid():
        access_querysd = query.access.doctor_set.all()
        for s in access_querysd:
            if s.suggestion == form.cleaned_data.get("suggestion"):
                context = {
                    'access': access,
                    'query': query,
                    'form': form,
                    'error_message': 'You already added that query',
                }
                return render(request, 'cancer/add_suggestion.html', context)
        doctor = form.save(commit=False)
        doctor.access = access
        doctor.query = query
        doctor.save()
        access=Access.objects.filter(user=request.user)
        query=Query.objects.filter(query=request.query)
        return render(request, 'cancer/detail.html', {'access': access, 'query': query})
    context = {
            'access': access,
            'query': query,
            'form': form,
    }
    return render(request, 'cancer/add_suggestion.html', context)


def suggestion(request, filter_by):
    if not request.user.is_authenticated():
        return render(request, 'cancer/add_suggestion.html')
    else:
        try:
            suggestion_ids = []
            for query in Access.objects.filter(user=request.user):
                for query in query.access.query_set.all():
                    suggestion_ids.append(query.pk)
            users_suggestion = Query.objects.filter(pk__in=suggestion_ids)
            if filter_by == 'favourites':
                users_suggestion = users_suggestion.filter(is_favorite=True)
        except Access.DoesNotExist:
            users_suggestion = []

        return render(request, 'cancer/query.html', {
            'query_list': users_suggestion,
            'filter_by': filter_by,
        })

