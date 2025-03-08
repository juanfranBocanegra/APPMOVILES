from .serializers import PostSerializer
from django.db.models.signals import post_delete, post_save, m2m_changed
from django.dispatch import receiver
from .models import Post, Challenge
from backend import settings

@receiver(m2m_changed, sender=Post)
@receiver(post_delete, sender=Post)
@receiver(post_save, sender=Post)
def update_rankings(sender, instance, **kwargs):

    challenges = Challenge.objects.all()

    for c in challenges:

        posts = Post.objects.filter(challenge=c).order_by('-points')[:settings.RANKING_SIZE]

        posts_data = PostSerializer(posts, many=True)

        c.ranking = posts_data.data

        c.save(update_fields=["ranking"])