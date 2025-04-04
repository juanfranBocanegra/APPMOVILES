from django.urls import path, include
from rest_framework.routers import DefaultRouter
from .views import  SignUpView, LoginView, CheckView,LogoutView, FollowView, \
                    ProfileView, FeedView, ChallengeView, PostView, VoteView, UnfollowView, \
                    SearchView, ApiRootView, ProfileImageView

urlpatterns = [
    path("", ApiRootView.as_view(), name="api-root"),
    path('signup/', SignUpView.as_view(), name='signup'),
    path('login/', LoginView.as_view(), name='login'),
    path('check/', CheckView.as_view(), name='check'),
    path('logout/', LogoutView.as_view(), name='logout'),
    path('follow/',FollowView.as_view(), name='follow'),
    path('follow/<str:username>',FollowView.as_view(), name='get_follow'),
    path('unfollow/',UnfollowView.as_view(), name='unfollow'),
    path('profile/',ProfileView.as_view(), name='profile'),
    path('profile/<str:username>',ProfileView.as_view(), name='get_profile'),
    path('profile/image/', ProfileImageView.as_view(), name='profile-image'),
    path('feed/<int:size>',FeedView.as_view(), name='feed'),
    path('challenges/',ChallengeView.as_view(),name='challenges'),
    path('post/',PostView.as_view(), name='post'),
    path('vote/',VoteView.as_view(), name='vote'),
    path('search/<str:text>',SearchView.as_view(), name='search'),
]
