package com.app.dolt  // Cambia por tu paquete real

import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import timber.log.Timber

class MyFirebaseMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        Log.d("FCM", "Token actualizado: $token")
        // Guarda el token en tu servidor si es necesario
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d("FCM", "Mensaje recibido: ${remoteMessage.notification?.title}")



        // 1. Notificación en segundo plano/foreground
        remoteMessage.notification?.let { notif ->
            showNotification(notif.title ?: "Sin título", notif.body ?: "Sin contenido")
        }

        // 2. Datos personalizados (opcional)
        remoteMessage.data.let { data ->
            if (data.isNotEmpty()) {
                Log.d("FCM", "Datos: $data")
            }
        }
    }

    private fun showNotification(title: String, body: String) {
        val channelId = "global"  // Usa el mismo que en el manifiesto



        val notificationBuilder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.drawable.ic_dialog_info)  // Cambia por tu ícono
            .setContentTitle(title)
            .setContentText(body)
            .setAutoCancel(true)

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        // Canal de notificación (requerido en Android 8+)
        val channel = NotificationChannel(
            channelId,
            "Challenges",
            NotificationManager.IMPORTANCE_HIGH
        )
        notificationManager.createNotificationChannel(channel)
        Timber.i("AQUIAQUI")
        notificationManager.notify(1, notificationBuilder.build())
    }
}