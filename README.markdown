# Dolt - Red Social de Retos

## Descripción

Dolt es un proyecto personal que consiste en una red social de Retos, tiene inspiración en BeReal y Twitter, 
este repositorio incluye un Backend Djnago y una aplicación Android Kotlin de Frontend.

## Características

- Publica y responde a retos diarios
- Vota y opina sobre los retos de los demás
- Sigue a tus amigos y echa un vistazo a sus publiaciones
- Más adelante se añadirían más funciones como un ranking global o la publicación de imagenes
  
## Tecnologías

### Frontend (Android)

- **Lenguaje:** Kotlin `2.0.21`
- **Frameworks:** Jetpack (ViewModel, LiveData, Navigation)
- **UI:** Material Components, ConstraintLayout, SwipeRefreshLayout
- **Persistencia local:** Room `2.7.1`  
- **Binding:** DataBinding, ViewBinding
- **Redes:** Retrofit + OkHttp + Gson
- **Firebase:**
  - Firebase Auth
  - Firebase Cloud Messaging (FCM)
  - Firebase Analytics
- **Autenticación Google:** `credentials-play-services-auth:1.5.0`
- **Otras libs:** Glide `4.16.0`, Timber, Google Play Services

### Backend (Django)

- **Framework principal:** Django `5.1.7`
- **API REST:** Django REST Framework `3.15.2`
- **JWT Authentication:** djangorestframework-simplejwt `5.4.0`
- **Servidor ASGI:** Uvicorn `0.34.0`
- **Servidor WSGI:** Gunicorn `23.0.0`
- **Base de datos:** PostgreSQL (`psycopg2==2.9.10`)
- **Firebase Admin SDK:** `6.7.0`
- **Almacenamiento y Firestore:** `google-cloud-storage`, `google-cloud-firestore`
- **Despliegue de archivos estáticos:** WhiteNoise `6.9.0`

## Instalación

### Backend (Django)
1. Clona el repositorio:
   ```bash
   git clone https://github.com/juanfranBocanegra/Dolt.git
   cd Dolt/backend
   ```
2. Crea y activa un entorno virtual:
   ```bash
   python -m venv venv
   source venv/bin/activate  # En Windows: venv\Scripts\activate
   ```
3. Instala las dependencias:
   ```bash
   pip install -r requirements.txt
   ```
4. Configura las variables de entorno:
   - [Especifica las variables necesarias, ej. DATABASE_URL, SECRET_KEY, etc.]
5. Aplica las migraciones:
   ```bash
   python manage.py migrate
   ```
6. Inicia el servidor:
   ```bash
   python manage.py runserver
   ```

### Frontend (Kotlin)
1. Abre el directorio `frontend` en Android Studio
2. Sincroniza el proyecto con Gradle:
   - [Especifica cómo, ej. "Haz clic en 'Sync Project with Gradle Files' en Android Studio."]
3. Configura la URL del backend:
   - [Indica dónde y cómo configurar la conexión al backend, ej. en un archivo de configuración.]
4. Ejecuta la app:
   - [Especifica cómo, ej. en un emulador o dispositivo Android.]

## Uso

[Explica cómo usar la aplicación. Por ejemplo: "Regístrate en la app, inicia sesión y comienza a [acción principal, ej. publicar contenido]. Los endpoints principales de la API están en [URL base, ej. http://localhost:8000/api/]. Consulta [ruta o documentación] para más detalles."]

## Maquetas de la Aplicación

[Incluye aquí las maquetas de la app. Sube las imágenes al repositorio y reemplaza los marcadores de posición con las rutas correctas. Ejemplo:]

![Maqueta 1](ruta/a/maqueta1.png)  
*Descripción: [Breve descripción de lo que muestra la maqueta 1].*

![Maqueta 2](ruta/a/maqueta2.png)  
*Descripción: [Breve descripción de lo que muestra la maqueta 2].*

## Contacto

[Proporciona información de contacto o enlaces. Ejemplo: "Para preguntas o sugerencias, contacta a [tu nombre] en [correo o GitHub]."]
