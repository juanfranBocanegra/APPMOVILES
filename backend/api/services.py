from firebase_admin import messaging

def send_notification_to_all(title_en, title_es, body_en, body_es):
    """
    Envía una notificación a todos los dispositivos Android suscritos al tema 'todos'
    con títulos y cuerpos localizados en inglés y español.
    """
    message = messaging.Message(
        notification=messaging.Notification(
            title=title_en,  # Título por defecto (inglés)
            body=body_en,    # Cuerpo por defecto (inglés)
        ),
        topic='global',  # Enviar a todos los dispositivos suscritos al tema "todos"
        android=messaging.AndroidConfig(
            notification=messaging.AndroidNotification(
                title_loc_key="title_key",        # Clave de localización del título
                body_loc_key="body_key",          # Clave de localización del cuerpo
                title_loc_args=[title_en, title_es],  # Argumentos de localización del título
                body_loc_args=[body_en, body_es],     # Argumentos de localización del cuerpo
            )
        )
    )

    try:
        response = messaging.send(message)
        print(f"Notificación enviada con éxito: {response}")
        return response
    except Exception as e:
        print(f"Error al enviar notificación: {e}")
        raise