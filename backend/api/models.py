from django.db import models
from django.contrib.auth.models import AbstractUser
from datetime import datetime
import uuid
from django.db.models import JSONField



from backend import settings

class User(AbstractUser):  # Extiende el modelo User
    name = models.CharField(max_length=200)
    num_followers = models.IntegerField(default=0)
    num_followed = models.IntegerField(default=0)
    coins = models.IntegerField(default=0)
    vote_times = models.IntegerField(default=0)
    current_vote = JSONField(default=list, blank=True)
    vote_closed = models.BooleanField(default=False)

    def __str__(self):
        return self.username


class UserFollow(models.Model):
    follower = models.ForeignKey(User, related_name="following", on_delete=models.CASCADE) #Persona que le sigue
    followed = models.ForeignKey(User, related_name="followers", on_delete=models.CASCADE) #Persona que es seguida

    class Meta:
        unique_together = ('follower', 'followed')

class Challenge(models.Model):
    id = models.UUIDField(default=uuid.uuid4, editable=False, unique=True, primary_key=True)
    name = models.CharField(max_length=200, null=True)
    name_es = models.CharField(max_length=200, null=True)
    detail = models.TextField(max_length=200, null=True)
    detail_es = models.TextField(max_length=200, null=True)
    num_posts = models.IntegerField(default=0)
    #init_time
    #final_time

    ranking = JSONField(default=list, blank=True)

class Post(models.Model):
    id = models.UUIDField(default=uuid.uuid4, editable=False, unique=True, primary_key=True)
    user = models.ForeignKey(User, related_name="posted", on_delete=models.CASCADE, null=True)
    text = models.TextField(null=True)
    #image = ...
    date = models.DateTimeField(default=datetime.now, blank=True)
    challenge = models.ForeignKey('Challenge', on_delete=models.RESTRICT, null=True, blank=True)
    points = models.IntegerField(default=0)

    class Meta:
        ordering = ['-date']




