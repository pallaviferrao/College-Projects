from django.conf.urls import url

from cancersupport import settings
from . import views
from django.conf.urls.static import static

app_name = 'cancer'





#urlpatterns = [
 #   url(r'^$', views.IndexView.as_view(), name='index'),

  #  url(r'^register/$', views.UserFormView.as_view(), name='register'),

  #  url(r'^(?P<pk>[0-9]+)/$', views.DetailView.as_view(), name='detail'),

   # url(r'^User/add/$', views.UserCreate.as_view(), name='User-add'),

    #url(r'User/(?P<pk>[0-9]+)/$', views.UserUpdate.as_view(), name='User-update'),

    #url(r'User/(?P<pk>[0-9]+)/delete/$', views.UserDelete.as_view(), name='User-delete'),

    # url(r'^profile/$',views.ProfileView.as_view(), name='profile'),

    #url(r'^login_user/$', views.login_user, name='login_user'),
    #url(r'^logout_user/$', views.logout_user, name='logout_user'),
#url(r'^(?P<query_id>[0-9]+)/favorite/$', views.favorite, name='favorite'),
#url(r'^(?P<access_id>[0-9]+)/favorite_album/$', views.favorite_album, name='favorite_album'),



#]

urlpatterns = [
    url(r'^$', views.index, name='index'),
    url(r'^$', views.home, name='home'),
    url(r'^register/$', views.register, name='register'),
    url(r'^login_user/$', views.login_user, name='login_user'),
    url(r'^logout_user/$', views.logout_user, name='logout_user'),
    url(r'^(?P<access_id>[0-9]+)/$', views.detail, name='detail'),
    url(r'^query/(?P<filter_by>[a-zA_Z]+)/$', views.query, name='query'),
    url(r'^create_access/$', views.create_access, name='create_access'),
    url(r'^(?P<access_id>[0-9]+)/create_query/$', views.create_query, name='create_query'),
    url(r'^(?P<access_id>[0-9]+)/delete_query/(?P<query_id>[0-9]+)/$', views.delete_query, name='delete_query'),
    url(r'^(?P<access_id>[0-9]+)/delete_access/$', views.delete_access, name='delete_access'),
    url(r'^(?P<access_id>[0-9]+)/add_suggestion/(?P<query_id>[0-9]+)/$', views.add_suggestion, name='add_suggestion'),
    url(r'^suggestion/(?P<filter_by>[a-zA_Z]+)/$', views.suggestion, name='suggestion'),
] + static(settings.STATIC_URL)