package com.nuclan2024.myapplication

import android.content.Context
import android.icu.util.Calendar
import androidx.work.Data
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class NotificationScheduler {
    companion object {
        fun rescheduleNotification(context: Context, newHour: Int, newMinute: Int, notificationText: String) {

            WorkManager.getInstance(context).cancelAllWorkByTag("homework_reminder")

            val delay = calculateDelayUntilTime(newHour, newMinute)

            val inputData = Data.Builder()
                .putString("notification_text", notificationText)
                .build()

            val notificationWorkRequest = OneTimeWorkRequest.Builder(NotificationWorker::class.java)
                .setInputData(inputData)
                .setInitialDelay(delay, TimeUnit.MILLISECONDS)
                .addTag("homework_reminder")
                .build()

            WorkManager.getInstance(context).enqueueUniqueWork(
                "homework_reminder_work",
                ExistingWorkPolicy.REPLACE,
                notificationWorkRequest
            )
        }

        private fun calculateDelayUntilTime(hour: Int, minute: Int): Long {
            val calendar = Calendar.getInstance()
            val currentTime = calendar.timeInMillis

            calendar.set(Calendar.HOUR_OF_DAY, hour)
            calendar.set(Calendar.MINUTE, minute)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)

            if (calendar.timeInMillis <= currentTime) {
                calendar.add(Calendar.DAY_OF_MONTH, 1)
            }

            return calendar.timeInMillis - currentTime
        }
    }
}