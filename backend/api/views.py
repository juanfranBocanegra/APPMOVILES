from typing import OrderedDict
from rest_framework import generics, status
from rest_framework.response import Response
from rest_framework.permissions import AllowAny, IsAuthenticated
from rest_framework.authentication import BaseAuthentication
from rest_framework_simplejwt.tokens import RefreshToken
from .serializers import SignUpSerializer
from django.contrib.auth import authenticate
from django.contrib.auth import get_user_model
from .models import UserFollow, Post, Challenge
from .serializers import UserSimpleSerializer, UserProfileSerializer, PostSerializer, ChallengeSerializer
from django.db.models import F, Q
from backend import settings
import random
from .api_root import ApiRootView
from rest_framework.parsers import MultiPartParser

from firebase_admin import auth

User = get_user_model()

def get_resource_uri(request, res):
    url = str(request.build_absolute_uri())
    split = url.split('/')
    return split[0] + "//" + split[2] + res

class NoAuthentication(BaseAuthentication):
    def authenticate(self, request):
        return None  # No autentica al usuario


class GoogleLoginView(generics.GenericAPIView):
    permission_classes = [AllowAny]
    authentication_classes = [NoAuthentication]


    def post(self, request):
      
        id_token = request.data.get("id_token")  # Recibimos el token de Firebase desde el frontend

        print(id_token)

        if not id_token:
            return Response({"error": "Missing ID token"}, status=status.HTTP_400_BAD_REQUEST)

        #try:
        # Verificar el token con Firebase
        decoded_token = auth.verify_id_token(id_token)
        uid = decoded_token["uid"]
        email = decoded_token.get("email", "")

        email_first_part = email.split("@")[0]
        username = email_first_part + "_google"

        user, created = User.objects.get_or_create(username=uid, defaults={"name":email_first_part,"username": username})

        # Generar JWT de Django para la sesión
        refresh = RefreshToken.for_user(user)

        return Response({
            "refresh": str(refresh),
            "access": str(refresh.access_token),
            "user": {
                "id": user.id,
                "username": user.username,
                "email": user.email,
            }
        })
        #except auth.InvalidIdTokenError:
        #    return Response({"error": "Invalid token"}, status=status.HTTP_401_UNAUTHORIZED)



class CheckView(generics.GenericAPIView):
    permission_classes = [IsAuthenticated]

    def get(self, request):
        return Response({}, status=status.HTTP_200_OK)

# Registro de usuarios
class SignUpView(generics.CreateAPIView):
    queryset = User.objects.all()
    permission_classes = [AllowAny]
    authentication_classes = [NoAuthentication]
    serializer_class = SignUpSerializer

# Login de usuario con JWT
class LoginView(generics.GenericAPIView):
    permission_classes = [AllowAny]
    authentication_classes = [NoAuthentication]

    def post(self, request):
        username = request.data.get("username")
        password = request.data.get("password")

        print("LOGIN")
        
        user = authenticate(username=username, password=password)

        if user is not None:
            refresh = RefreshToken.for_user(user)
            #print(refresh.access_token)
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
            #print("AAAAAAAAAAAAAA",refresh_token)
            token = RefreshToken(refresh_token)
            print("LOGOUT")
            token.blacklist()
            return Response(status=status.HTTP_205_RESET_CONTENT)
        except Exception as e:
            print(e)
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
    
    def get(self, request, username):
        user = User.objects.filter(username=username)[0]
        if user is None:
            return Response({"detail":"Bad request"}, status=status.HTTP_400_BAD_REQUEST)

        followers = user.followers.select_related('follower').all()
        following = user.following.select_related('followed').all()

        followers_users = [follow.follower for follow in followers]
        following_users = [follow.followed for follow in following]

        
        followers_serializer = UserSimpleSerializer(followers_users, many=True)
        following_serializer = UserSimpleSerializer(following_users, many=True)

        

        return Response({"followers":followers_serializer.data, "following":following_serializer.data})


class UnfollowView(generics.GenericAPIView):
    permission_classes = [IsAuthenticated]

    def post(self, request):
        user = request.user  # Obtiene el usuario autenticado
        username = request.data.get("username")
        try:
            followed = User.objects.get(username=username)
        except User.DoesNotExist:
            return Response({"detail": "User not found."}, status=status.HTTP_404_NOT_FOUND)
        
        if user == followed:
            return Response({"detail": "You cannot unfollow yourself."}, status=status.HTTP_400_BAD_REQUEST)
        
        # Crear el registro de seguimiento
        try:
            follow = UserFollow.objects.filter(follower=user, followed=followed)
            follow.delete()
            user.num_followed = F('num_followed') - 1
            followed.num_followers = F('num_followers') - 1
            user.save(update_fields=["num_followed"])
            followed.save(update_fields=["num_followers"])
        except:
            return Response({"detail": "Duplicated follow."})
        return Response({"detail": f"Following {followed.username}"}, status=status.HTTP_201_CREATED)



class ProfileView(generics.GenericAPIView):
    permission_classes = [IsAuthenticated]

    def get(self, request, username):
       
        try:
            my_user : User = request.user
            user = User.objects.get(username=username)
        except:
            return Response({"detail":"Bad request"}, status=status.HTTP_400_BAD_REQUEST)
        
        followers = user.followers.select_related('follower').all()
        following = user.following.select_related('followed').all()

        followers_users = [follow.follower for follow in followers]
        following_users = [follow.followed for follow in following]

        user.num_followers = len(followers_users)
        user.num_followed = len(following_users)
        user.save()

        print("AQUI")

        user_data = UserProfileSerializer(user)

        following = UserFollow.objects.filter(Q(follower=my_user)&Q(followed=user))
        follower = UserFollow.objects.filter(Q(follower=user)&Q(followed=my_user))

        following = len(following) != 0
        follower = len(follower) != 0

        data = user_data.data
        data["following"] = following
        data["follower"] = follower

        #data["profile_image"] = get_resource_uri(request,data["profile_image"])

        return Response(data)

    """
    {"update":[{"name":"newname", 
                "profile_image":"newpicture", 
                "password":["old_password","new_password1","new_password2"]}]
    }
    
    """
    
    def post(self, request):
        user = request.user
        flag = 0

        
        try:

            

            if "name" in request.data.keys():
                if len(request.data.get("name")) > 4:
                    user.name = request.data.get("name")
                    user.save(update_fields=["name"])
                    flag = 1
            

            if "password" in request.data.keys():
                if len(request.data.get("password")[1]) >= 4:
                    old_password = request.data.get("password")[0]
                    check = authenticate(username=user.username, password=old_password)
                    if check is None:
                        return Response({"detail":"Wrong password"},status=status.HTTP_401_UNAUTHORIZED)
                    if request.data.get("password")[1] != request.data.get("password")[2]:
                        return Response({"detail":"The two passwords are not the same"},status=status.HTTP_400_BAD_REQUEST)

                    print(request.data.get("password")[1])
                    user.set_password(request.data.get("password")[1])
                    user.save()
                    flag = 1
        except Exception as e:
            print(e)
            if flag == 0:
                return Response({"detail":"Bad request"}, status=status.HTTP_400_BAD_REQUEST)

        data = UserProfileSerializer(user).data
        
        #data["profile_image"] = get_resource_uri(request,data["profile_image"])

        return Response(data)

    def delete(self, request):
        user = request.user

        user.delete()
        
        return Response(status=status.HTTP_204_NO_CONTENT)


class ProfileImageView(generics.GenericAPIView):
    parser_classes = [MultiPartParser]  # Permite recibir multipart/form-data
    permission_classes = [IsAuthenticated]

    def post(self, request):
        print("IMAGEN")
        user = request.user
        if "profile_image" not in request.FILES:
            return Response({"error": "No image provided"}, status=400)

        # Borrar imagen anterior si no es la default
        if user.profile_image and not user.profile_image.name.endswith("default.png"):
            user.profile_image.delete(save=False)

        # Guardar nueva imagen
        user.profile_image = request.FILES["profile_image"]
        user.save(update_fields=["profile_image"])

        return Response({"status": "Image updated successfully"}, status=200)


class FeedView(generics.GenericAPIView):
    permission_classes = [IsAuthenticated]
    
    def get(self, request, size):
        user = request.user
        size = int(size)
        following = user.following.select_related('followed').all() 
        following_users = [follow.followed for follow in following]
        posts = Post.objects.filter(Q(user__in=following_users)|Q(user=user)).order_by('-date')
        #posts = Post.objects.all().order_by('-date')

        if size != 0:
            posts = posts[:size]

        
        post_data = PostSerializer(posts, many=True)

        #print(post_data.data)

        return Response(post_data.data)
    
class ChallengeView(generics.GenericAPIView):
    permission_classes = [IsAuthenticated]
    queryset = Challenge.objects.all()

    def get(self, request):
        #print(request.auth)
        user = request.user
        challenges = Challenge.objects.all()
        all_data = []
        for challenge in challenges:
            post = Post.objects.filter(user=user, challenge=challenge)
            challenge_data = ChallengeSerializer(challenge).data
            challenge_data['available'] = len(post) == 0
            all_data.append(challenge_data)

        
        return Response(all_data)


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


class SearchView(generics.GenericAPIView):
    permission_classes = [IsAuthenticated]

    def get(self, request, text : str):
        user = request.user
        text = text.lower()
        print("Search text: ",text)
        my_users = []

        if text in user.username.lower() or text in user.name.lower():
            
            my_users.append(user)

        followers = user.followers.select_related('follower').all()
        following = user.following.select_related('followed').all()

        followers_users = [follow.follower for follow in followers]
        following_users = [follow.followed for follow in following]


        for u in followers_users+following_users:
            if text in u.name.lower() or text in u.username.lower():
                if u not in my_users:
                    my_users.append(u)
                

        list_users = User.objects.filter(Q(username__icontains=text)|Q(name__icontains=text))

       
        
        for u in list_users:
            if u not in my_users:
                my_users.append(u)
        
        my_users_data = UserSimpleSerializer(my_users, many=True)

        print(my_users_data.data)

        
        return Response(my_users_data.data,status=status.HTTP_200_OK)

