from django.contrib import admin
from .models import User, UserFollow, Post, Challenge
import json

@admin.register(User)
class UserAdmin(admin.ModelAdmin):
    list_display = ('name', 'username', 'num_followers', 'num_followed')

@admin.register(UserFollow)
class UserFollowAdmin(admin.ModelAdmin):
    list_display = ('follower', 'followed')

@admin.register(Post)
class PostAdmin(admin.ModelAdmin):
    list_display = ('user', 'text', 'date', 'challenge_name')

    def challenge_name(self, instance):
        if instance.challenge:
            return instance.challenge.name


@admin.register(Challenge)
class ChallengeAdmin(admin.ModelAdmin):
    list_display = ('name', 'detail', 'num_posts')
    fields = ['name', 'name_es', 'detail', 'detail_es', 'num_posts', 'ranking']
    
    def ranking(self, instance):
        if instance.ranking:
            data = json.loads(instance.ranking)
            return data
    

    


