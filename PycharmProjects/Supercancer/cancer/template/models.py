from django.db import models
from django.core.urlresolvers import reverse
from django.contrib.auth.models import Permission, User


class Access(models.Model):
    user = models.ForeignKey(User,default=1)
    name = models.CharField(max_length=250)
    cancer_type = models.CharField(max_length=250)
    blood_group = models.CharField(max_length=100)
    email = models.CharField(max_length=500)
    cancer_logo = models.FileField(max_length=2000)

    def get_absolute_url(self):
        return reverse('cancer:detail', kwargs={'pk': self.pk})

    def __repr__(self):
        return self.name + "-" + self.cancer_type


class Query(models.Model):
    access = models.ForeignKey(Access, on_delete=models.CASCADE,null=True,blank=True)
    query_title = models.CharField(max_length=1000)
    solution = models.CharField(max_length=1000)

    def __repr__(self):
        return self.solution

class Doctor(models.Model):
    access = models.ForeignKey(Access, on_delete=models.CASCADE,null=True,blank=True)
    query = models.ForeignKey(Query, on_delete=models.CASCADE,null=True,blank=True)
    doctor_name = models.CharField(max_length=2000)
    suggestion = models.CharField(max_length=2000)

    def __repr__(self):
        return self.suggestion



                # class UserProfile(models.Model):
        #   user = models.OneToOneField(User)
        #  description = models.CharField(max_length=100, default="")
        # phone = models.IntegerField(default=0)
        # history = models.FileField(max_length=1000)

        # def create_profile(sender, **kwargs):
        #       if kwargs['created']:
        #          user_profile = UserProfile.objects.create(user= kwargs['instance'])

# post_save.connect(create_profile,sender=User)
