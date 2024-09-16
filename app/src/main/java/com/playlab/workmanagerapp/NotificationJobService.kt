package com.playlab.workmanagerapp

import android.annotation.SuppressLint
import android.app.job.JobParameters
import android.app.job.JobService

@SuppressLint("SpecifyJobSchedulerIdRange")
class NotificationJobService : JobService() {
    override fun onStartJob(params: JobParameters?): Boolean {
        // Mostrar a notificação
        ShowNotification(
            context = this,
            channelId = NotificationChannelsIds.JOB_SCHEDULER_CHANNEL_ID,
            title = "JobScheduler",
            message = "Tarefa executada com JobScheduler!"
        )
        return false // Indica que a tarefa foi concluída imediatamente
    }

    override fun onStopJob(params: JobParameters?): Boolean {
        return false // Não precisa reagendar a tarefa
    }
}
