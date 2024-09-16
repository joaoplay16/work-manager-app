package com.playlab.workmanagerapp

import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat

object ShowNotification {
    operator fun invoke(
        context: Context,
        channelId: String,
        title: String,
        message: String
    ) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val notification = NotificationCompat.Builder(
            context,
            channelId
        )
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(
            1,
            notification
        )
    }
}