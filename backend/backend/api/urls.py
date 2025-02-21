from django.urls import path
from .views import SignUpView, LoginView, LogoutView, FollowView, ProfileView, FeedView, PostView, VoteView

urlpatterns = [
    path('signup/', SignUpView.as_view(), name='register'),
    path('login/', LoginView.as_view(), name='login'),
    path('logout/', LogoutView.as_view(), name='logout'),
    path('follow/',FollowView.as_view(), name='follow'),
    #path('unfollow/',FollowView.as_view(), name='follow'),
    path('profile/',ProfileView.as_view(), name='profile'),
    path('feed/',FeedView.as_view(), name='feed'),
    path('post/',PostView.as_view(), name='post'),
    path('vote/',VoteView.as_view(), name='vote')
]
