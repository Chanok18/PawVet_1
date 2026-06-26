package com.example.pawvet_1.ui.notifications

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class ReminderWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val title = inputData.getString("input_title").orEmpty()
        val body = inputData.getString("input_body").orEmpty()
        val route = inputData.getString("input_route").orEmpty()
        val notificationId = inputData.getInt("input_notification_id", System.currentTimeMillis().toInt())

        NotificationHelper.showNotification(
            context = applicationContext,
            notificationId = notificationId,
            title = title,
            body = body,
            targetRoute = route
        )
        return Result.success()
    }
}
