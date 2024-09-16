package com.playlab.workmanagerapp

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.playlab.workmanagerapp.NotificationChannelsIds.ALARM_MANAGER_CHANNEL_ID
import com.playlab.workmanagerapp.NotificationChannelsIds.DOWNLOAD_CHANNEL_ID
import com.playlab.workmanagerapp.NotificationChannelsIds.JOB_SCHEDULER_CHANNEL_ID

class DownloadApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val chanel = NotificationChannel(
                DOWNLOAD_CHANNEL_ID,
                "File download",
                NotificationManager.IMPORTANCE_HIGH
            )

            val alarmManagerChannel = NotificationChannel(
                ALARM_MANAGER_CHANNEL_ID,
                "Alarm Manager Channel",
                NotificationManager.IMPORTANCE_HIGH
            )

            val jobSchedulerChannel = NotificationChannel(
                JOB_SCHEDULER_CHANNEL_ID,
                "Job Scheduler channel",
                NotificationManager.IMPORTANCE_HIGH
            )

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannels(
                listOf(
                    chanel,
                    alarmManagerChannel,
                    jobSchedulerChannel
                )
            )
        }
    }
}