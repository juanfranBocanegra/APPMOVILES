from django.contrib import admin
from .models import User, UserFollow, Post, Challenge
import json
from django.utils.safestring import mark_safe

@admin.register(User)
class UserAdmin(admin.ModelAdmin):
    list_display = ('name', 'username','profile_image_tag', 'num_followers', 'num_followed')
    fields = ['name', 'username', 'profile_image', 'num_followers', 'num_followed', 'coins', 'vote_times', 'current_vote', 'vote_closed']
    readonly_fields = ['num_followers', 'num_followed','profile_image_tag']
    search_fields = ['name', 'username']

    def profile_image_tag(self, instance):
        if instance.profile_image:
            return mark_safe(f'<img src="{instance.profile_image.url}" width="100" height="100" />')
        return ""
    profile_image_tag.short_description = 'Profile Image Preview'

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
    

    


