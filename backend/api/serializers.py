from rest_framework import serializers
from rest_framework.validators import UniqueValidator
from django.contrib.auth.password_validation import validate_password
from django.contrib.auth import get_user_model
from .models import UserFollow, User, Post, Challenge

User = get_user_model()

class UserSimpleSerializer(serializers.ModelSerializer):
    
    class Meta:
        model = User
        fields = ["name","username"]

class UserProfileSerializer(serializers.ModelSerializer):

    class Meta:
        model = User
        fields = ["name", "username", "num_followers", "num_followed"]

class SignUpSerializer(serializers.ModelSerializer):
    
    password = serializers.CharField(write_only=True, required=True, validators=[validate_password])
    password2 = serializers.CharField(write_only=True, required=True)

    class Meta:
        model = User
        fields = ('username', 'name', 'password', 'password2')

    def validate(self, attrs):
        if attrs['password'] != attrs['password2']:
            raise serializers.ValidationError({"password": "Las contraseñas no coinciden"})
        return attrs

    def create(self, validated_data):
        user = User.objects.create(
            username=validated_data['username'],
            name=validated_data['name']
        )
        user.set_password(validated_data['password'])
        user.save()
        return user

class UserFollowSerializer(serializers.ModelSerializer):
    class Meta:
        model = UserFollow
        fields = ['follower', 'followed']


class PostSerializer(serializers.ModelSerializer):
    class Meta:
        model = Post          #image
        fields = ['id','user','text','date','challenge']

    # Sobreescribe el método `to_representation` para incluir el username
    def to_representation(self, instance):
        representation = super().to_representation(instance)
        # Accede al username del usuario relacionado
        representation['user'] = instance.user.username
        representation['name_user'] = instance.user.name
        representation['challenge'] = 'NULL'
        representation['challenge_es'] = 'NULL'
        if instance.challenge:
            representation['challenge'] = instance.challenge.name
            representation['challenge_es'] = instance.challenge.name_es
            

        return representation

class ChallengeSerializer(serializers.ModelSerializer):
    class Meta:
        model = Challenge
        fields = ['id', 'name', 'name_es', 'detail', 'detail_es']