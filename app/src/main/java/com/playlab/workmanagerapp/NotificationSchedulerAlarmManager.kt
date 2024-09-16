package com.playlab.workmanagerapp

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class NotificationSchedulerAlarmManager {

    companion object {
        fun scheduleNotification(
            context: Context,
            interval: Long
        ) {
            val alarmManager =
                context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(
                context,
                AlarmReceiver::class.java
            )
            val pendingIntent = PendingIntent.getBroadcast(
                context,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE
            )

            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP,
                System.currentTimeMillis() + interval,
                interval,
                pendingIntent
            )
        }
    }
}

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(
        context: Context,
        intent: Intent
    ) {
        // Mostrar a notificação
        ShowNotification(
            context,
            channelId = NotificationChannelsIds.ALARM_MANAGER_CHANNEL_ID,
            title = "AlarmManager",
            message = "Tarefa executada com AlarmManager!"
        )
    }
}