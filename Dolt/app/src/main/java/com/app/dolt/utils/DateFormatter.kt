package com.app.dolt.utils

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

/**
 * Objeto utilitario para formatear fechas recibidas desde la API.
 * Proporciona funcionalidad para convertir fechas ISO 8601 a formatos legibles
 * según el tiempo transcurrido desde la fecha original.
 */
object DateFormatter {

    /**
     * Formatea una fecha ISO 8601 recibida desde la API a un formato legible
     * según las siguientes reglas:
     * - Si es menor a 24 horas: muestra solo la hora (ej. "14:30")
     * - Si es menor a 1 año: muestra día, mes y hora (ej. "3 Abr 14:30")
     * - Si es mayor a 1 año: muestra día, mes y año (ej. "3 Abr 2023")
     *
     * @param apiDate Cadena de fecha en formato ISO 8601 (ej. "2023-04-03T14:30:00.000Z")
     * @return String formateado según las reglas especificadas
     *
     * @throws ParseException si la cadena de fecha no puede ser parseada (aunque devuelve el string original en este caso)
     *
     * @sample formatApiDate("2023-04-03T14:30:00.000Z") podría devolver "14:30" si es reciente
     */
    @SuppressLint("SimpleDateFormat") // Se usa formato simple para el parsing inicial
    fun formatApiDate(apiDate: String): String {
        // Parsear la fecha ISO 8601 desde la API
        val utcFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").apply {
            timeZone = TimeZone.getTimeZone("UTC")
        }

        // Obtener fecha en UTC (eliminando milisegundos si existen)
        val cleanDateString = apiDate.substringBefore(".") + "Z"
        val date = utcFormat.parse(cleanDateString) ?: return apiDate

        // Convertir a zona horaria local
        val localCalendar = Calendar.getInstance().apply {
            time = date
        }

        val now = Calendar.getInstance()
        val diffInMillis = now.timeInMillis - localCalendar.timeInMillis

        return when {
            // Menos de 24 horas -> solo hora
            diffInMillis < TimeUnit.DAYS.toMillis(1) -> {
                SimpleDateFormat("HH:mm", Locale.getDefault()).format(localCalendar.time)
            }
            // Menos de 1 año -> día, mes y hora
            diffInMillis < TimeUnit.DAYS.toMillis(365) -> {
                SimpleDateFormat("d MMM HH:mm", Locale.getDefault()).format(localCalendar.time)
            }
            // Más de 1 año -> fecha completa
            else -> {
                SimpleDateFormat("d MMM yyyy", Locale.getDefault()).format(localCalendar.time)
            }
        }
    }
}