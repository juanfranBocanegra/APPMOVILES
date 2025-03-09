from django.urls import path
from .views import  SignUpView, LoginView, CheckView,LogoutView, FollowView, \
                    ProfileView, FeedView, ChallengeView, PostView, VoteView

urlpatterns = [
    path('signup/', SignUpView.as_view(), name='register'),
    path('login/', LoginView.as_view(), name='login'),
    path('check/', CheckView.as_view(), name='check'),
    path('logout/', LogoutView.as_view(), name='logout'),
    path('follow/',FollowView.as_view(), name='follow'),
    #path('unfollow/',FollowView.as_view(), name='follow'),
    path('profile/',ProfileView.as_view(), name='profile'),
    path('profile/<str:username>',ProfileView.as_view(), name='profile'),
    path('feed/',FeedView.as_view(), name='feed'),
    path('challenges/',ChallengeView.as_view(),name='challenges'),
    path('post/',PostView.as_view(), name='post'),
    path('vote/',VoteView.as_view(), name='vote')
]
