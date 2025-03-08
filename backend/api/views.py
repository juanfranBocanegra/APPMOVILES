from rest_framework import generics, status
from rest_framework.response import Response
from rest_framework.permissions import AllowAny, IsAuthenticated
from rest_framework_simplejwt.tokens import RefreshToken
from .serializers import SignUpSerializer
from django.contrib.auth import authenticate
from django.contrib.auth import get_user_model
from .models import UserFollow, Post, Challenge
from .serializers import UserSimpleSerializer, UserProfileSerializer, PostSerializer, ChallengeSerializer
from django.db.models import F, Q
from backend import settings
import random
from operator import itemgetter

User = get_user_model()

# Registro de usuarios
class SignUpView(generics.CreateAPIView):
    queryset = User.objects.all()
    permission_classes = [AllowAny]
    serializer_class = SignUpSerializer

# Login de usuario con JWT
class LoginView(generics.GenericAPIView):
    permission_classes = [AllowAny]

    def post(self, request):
        username = request.data.get("username")
        password = request.data.get("password")
        user = authenticate(username=username, password=password)

        if user is not None:
            refresh = RefreshToken.for_user(user)
            return Response({
                "refresh": str(refresh),
                "access": str(refresh.access_token),
            })
        return Response({"error": "Wrong credentials"}, status=status.HTTP_401_UNAUTHORIZED)

# Logout (revoca el token)
class LogoutView(generics.GenericAPIView):
    permission_classes = [IsAuthenticated]

    def post(self, request):
        try:
            refresh_token = request.data["refresh"]
            token = RefreshToken(refresh_token)
            token.blacklist()
            return Response(status=status.HTTP_205_RESET_CONTENT)
        except Exception as e:
            return Response(status=status.HTTP_400_BAD_REQUEST)

class FollowView(generics.GenericAPIView):
    permission_classes = [IsAuthenticated]

    def post(self, request):
        user = request.user  # Obtiene el usuario autenticado
        username = request.data.get("username")
        try:
            followed = User.objects.get(username=username)
        except User.DoesNotExist:
            return Response({"detail": "User not found."}, status=status.HTTP_404_NOT_FOUND)
        
        if user == followed:
            return Response({"detail": "You cannot follow yourself."}, status=status.HTTP_400_BAD_REQUEST)
        
        # Crear el registro de seguimiento
        try:
            UserFollow.objects.create(follower=user, followed=followed)
            user.num_followed = F('num_followed') + 1
            followed.num_followers = F('num_followers') + 1
            user.save(update_fields=["num_followed"])
            followed.save(update_fields=["num_followers"])
        except:
            return Response({"detail": "Duplicated follow."})
        return Response({"detail": f"Following {followed.username}"}, status=status.HTTP_201_CREATED)
    
    def get(self, request):
        user = request.user

        followers = user.followers.select_related('follower').all()
        following = user.following.select_related('followed').all()

        followers_users = [follow.follower for follow in followers]
        following_users = [follow.followed for follow in following]

        
        followers_serializer = UserSimpleSerializer(followers_users, many=True)
        following_serializer = UserSimpleSerializer(following_users, many=True)

        

        return Response({"followers":followers_serializer.data, "following":following_serializer.data})

class ProfileView(generics.GenericAPIView):
    permission_classes = [IsAuthenticated]

    def get(self, request):
        try:
            username = request.data.get("username")
            user = User.objects.get(username=username)
        except:
            return Response({"detail":"Bad request"}, status=status.HTTP_400_BAD_REQUEST)
        
        user_data = UserProfileSerializer(user)

        return Response(user_data.data)

    """
    {"update":[{"name":"newname", 
                "picture":"newpicture", 
                "password":["old_password","new_password1","new_password2"]}]
    }
    
    """
    
    def post(self, request):
        user = request.user

        update = request.data.get("update")[0]

        
        try:
            if "name" in update.keys():
                user.name = update["name"]
                user.save(update_fields=["name"])
            if "picture" in update.keys():
                pass
            if "password" in update.keys():
                old_password = update["password"][0]
                check = authenticate(username=user.username, password=old_password)
                if check is None:
                    return Response({"detail":"Wrong password"},status=status.HTTP_401_UNAUTHORIZED)
                if update["password"][1] != update["password"][2]:
                    return Response({"detail":"The two passwords are not the same"},status=status.HTTP_400_BAD_REQUEST)

                user.set_password(update["password"][1])
                user.save()
        except:
            return Response({"detail":"Bad request"}, status=status.HTTP_400_BAD_REQUEST)

        user_data = UserProfileSerializer(user)
        

        return Response(user_data.data)


class FeedView(generics.GenericAPIView):
    permission_classes = [IsAuthenticated]
    
    def get(self, request):
        user = request.user
        try:
            size = request.data.get("size")
            size = int(size)
        except:
            return Response({"detail":"Bad request"}, status=status.HTTP_400_BAD_REQUEST)
        following = user.following.select_related('followed').all()
        following_users = [follow.followed for follow in following]
        posts = Post.objects.filter(Q(user__in=following_users)|Q(user=user)).order_by('-date')[:size]

        
        post_data = PostSerializer(posts, many=True)

        return Response({"feed":post_data.data})
    
class ChallengeView(generics.GenericAPIView):
    permission_classes = [IsAuthenticated]
    queryset = Challenge.objects.all()

    def get(self, request):
        print(request.auth)
        challenges = Challenge.objects.all()
        challenge_data = ChallengeSerializer(challenges, many=True)

        return Response(challenge_data.data)

class PostView(generics.GenericAPIView):
    permission_classes = [IsAuthenticated]

    def post(self, request):
        user = request.user
        try:
            challenge_id = request.data.get("challenge")
            text = request.data.get("text")
        except:
            return Response({"detail":"Bad request"}, status=status.HTTP_400_BAD_REQUEST)
        #image = request.data.get("image")

        challenge = Challenge.objects.filter(id=challenge_id)[0]
        check = Post.objects.filter(user=user, challenge=challenge)
        if len(check) != 0:
            return Response({"detail":"Repeated post"})
        try:                                
                                            #image=image
            Post.objects.create(user=user,text=text,challenge=challenge)
            challenge.num_posts = F('num_posts') + 1
            challenge.save(update_fields=["num_posts"])
        except:
            pass

        user.vote_times = F('vote_times') + settings.VOTE_INCREMENT
        user.save(update_fields=["vote_times"])

        return Response({"detail":"OK"})


class VoteView(generics.GenericAPIView):
    permission_classes = [IsAuthenticated]

    def get(self, request):
        user = request.user 
        challenge = None
        if user.vote_times == 0:
            return Response({"detail":"No more vote times"})
        
        voting_posts = []
        
        if user.vote_closed == False:

            
            
            for post_id in user.current_vote:
                post = Post.objects.filter(id=post_id)[0]
                challenge = post.challenge
                voting_posts.append(post)

        else:
            challenge = Challenge.objects.order_by('?')[0]
            voting_posts = Post.objects.filter(challenge=challenge).order_by('?')[:settings.VOTE_SIZE]

        challenge_data = ChallengeSerializer(challenge)
        posts_data = PostSerializer(voting_posts, many=True)
        user.vote_closed = False
        voting_open = []
        for p in posts_data.data:
            voting_open.append(p["id"])
        user.current_vote = voting_open
        user.save(update_fields=["vote_closed","current_vote"])

        return Response({"challenge":challenge_data.data, "posts":posts_data.data})

    def post(self, request):
        user = request.user
        voted_posts_data = request.data.get("posts")
        challenge_data = request.data.get("challenge")
        challenge = Challenge.objects.filter(id=challenge_data["id"])[0]

        if user.vote_closed == True:
            return Response({"detail":"error, no vote"})

        voted_posts_id = []
        for p in voted_posts_data:
            voted_posts_id.append(p["id"])
        
        if sorted(user.current_vote) != sorted(voted_posts_id):
            return Response({"detail":"error, invalid vote"})

        
        for i,p in enumerate(voted_posts_id):
            post = Post.objects.filter(id=p)[0]
            increment = 0
            try:
                increment = settings.VOTE_POINTS[i]
            except:
                increment = 0
            post.points = F('points') + increment
            post.save(update_fields=["points"])

        user.coins = F('coins') + settings.VOTE_REWARD
        user.vote_times = max(F('vote_times') - 1, 0)
        user.vote_closed = True
        user.save(update_fields=["coins","vote_times","vote_closed"])

        return Response({"OK"})

