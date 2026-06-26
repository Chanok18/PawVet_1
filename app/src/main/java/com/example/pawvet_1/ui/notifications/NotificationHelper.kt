package com.example.pawvet_1.ui.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.example.pawvet_1.MainActivity
import com.example.pawvet_1.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

object NotificationHelper {
    const val CHANNEL_ID = "pawvet_reminders"
    const val CHANNEL_NAME = "Recordatorios PawVet"
    const val EXTRA_TARGET_ROUTE = "target_route"

    private const val INPUT_TITLE = "input_title"
    private const val INPUT_BODY = "input_body"
    private const val INPUT_ROUTE = "input_route"
    private const val INPUT_NOTIFICATION_ID = "input_notification_id"

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        val channel = NotificationChannel(
            CHANNEL_ID,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        ).apply {
            description = "Recordatorios locales y avisos importantes de PawVet"
        }

        val manager = context.getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(channel)
    }

    fun scheduleAppointmentReminder(
        context: Context,
        uniqueName: String,
        title: String,
        body: String,
        date: String,
        time: String,
        targetRoute: String
    ) {
        val triggerAt = calculateReminderTime(date, time)
        val delayMillis = (triggerAt.time - System.currentTimeMillis()).coerceAtLeast(5_000L)
        val notificationId = uniqueName.hashCode()

        val inputData = Data.Builder()
            .putString(INPUT_TITLE, title)
            .putString(INPUT_BODY, body)
            .putString(INPUT_ROUTE, targetRoute)
            .putInt(INPUT_NOTIFICATION_ID, notificationId)
            .build()

        val request = OneTimeWorkRequestBuilder<ReminderWorker>()
            .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
            .setInputData(inputData)
            .addTag(prefixFromUniqueName(uniqueName))
            .addTag(uniqueName)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            uniqueName,
            ExistingWorkPolicy.REPLACE,
            request
        )
    }

    fun cancelAllScheduledReminders(context: Context) {
        WorkManager.getInstance(context).cancelAllWorkByTag("cita")
        WorkManager.getInstance(context).cancelAllWorkByTag("servicio")
    }

    fun showNotification(
        context: Context,
        notificationId: Int,
        title: String,
        body: String,
        targetRoute: String
    ) {
        val intent = Intent(context, MainActivity::class.java).apply {
            putExtra(EXTRA_TARGET_ROUTE, targetRoute)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP
        }

        val pendingIntent = PendingIntent.getActivity(
            context,
            notificationId,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val notification = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(body)
            .setStyle(NotificationCompat.BigTextStyle().bigText(body))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        NotificationManagerCompat.from(context).notify(notificationId, notification)
    }

    fun buildReminderUniqueName(prefix: String, mascotaId: Int, date: String, time: String): String {
        return "$prefix-$mascotaId-$date-$time"
    }

    private fun calculateReminderTime(date: String, time: String): Date {
        val dateTime = parseDateTime(date, time) ?: return Date(System.currentTimeMillis() + 10_000L)
        val calendar = Calendar.getInstance().apply {
            this.time = dateTime
            add(Calendar.HOUR_OF_DAY, -1)
        }

        val oneHourBefore = calendar.timeInMillis
        return if (oneHourBefore > System.currentTimeMillis()) {
            Date(oneHourBefore)
        } else {
            Date(System.currentTimeMillis() + 10_000L)
        }
    }

    private fun parseDateTime(date: String, time: String): Date? {
        val formatter = SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.US)
        return runCatching {
            formatter.parse("$date $time")
        }.getOrNull()
    }

    private fun prefixFromUniqueName(uniqueName: String): String {
        return uniqueName.substringBefore("-")
    }
}
