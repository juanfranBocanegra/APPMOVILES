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


@receiver(m2m_changed, sender=Challenge)
@receiver(post_save, sender=Challenge)
def notify_on_new_challenge(sender, instance, created, **kwargs):
    """
    Enviar notificación cada vez que se cree un nuevo reto.
    """
    from api.services import send_notification_to_all
    if created:  # Solo enviamos la notificación si es un nuevo reto
        # Aquí puedes usar los campos de la instancia de 'Reto' para personalizar el mensaje
        title_en = f"New Challenge: {instance.name}"
        title_es = f"Nuevo reto: {instance.name_es}"
        body_en = f"A new challenge titled '{instance.name}' has been created!"
        body_es = f"Se ha creado un nuevo reto titulado '{instance.name_es}'!"

        # Enviar la notificación
        send_notification_to_all(title_en, title_es, body_en, body_es)