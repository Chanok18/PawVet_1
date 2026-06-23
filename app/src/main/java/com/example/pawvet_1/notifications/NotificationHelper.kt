package com.example.pawvet_1.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.text.SimpleDateFormat
import java.util.*

/**
 * HELPER: Se encarga de programar notificaciones locales.
 */
class NotificationHelper(private val context: Context) {

    fun programarRecordatorio(citaId: Int, fecha: String, hora: String, tipo: String) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, NotificationReceiver::class.java).apply {
            putExtra("titulo", "Recordatorio de Cita: $tipo")
            putExtra("mensaje", "Hoy tienes una cita a las $hora")
        }

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            citaId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        // Convertir fecha y hora a milisegundos
        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        val fechaCita = sdf.parse("$fecha $hora")

        fechaCita?.let {
            // Programamos la alarma (puedes restarle tiempo si quieres que suene antes)
            alarmManager.set(
                AlarmManager.RTC_WAKEUP,
                it.time,
                pendingIntent
            )
        }
    }
}
