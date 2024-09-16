import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.playlab.workmanagerapp.NotificationChannelsIds
import com.playlab.workmanagerapp.ShowNotification
import java.util.concurrent.TimeUnit

class NotificationWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : Worker(
    appContext,
    workerParams
) {

    override fun doWork(): Result {
        ShowNotification(
            applicationContext,
            channelId = NotificationChannelsIds.WORKER_SCHEDULER_CHANNEL_ID,
            title = "NotificationWorker",
            message = "Tarefa executada com NotificationWorker!"
        )
        // Agendar novamente a tarefa após 15 minutos
        scheduleWork(applicationContext)
        return Result.success()
    }


    companion object {
        fun scheduleWork(applicationContext: Context) {
            val workRequest = OneTimeWorkRequestBuilder<NotificationWorker>()
                .setInitialDelay(
                    15,
                    TimeUnit.MINUTES
                )
                .setConstraints(
                    Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                        .build()
                )
                .build()

            WorkManager.getInstance(applicationContext).enqueueUniqueWork(
                "notificacao_trabalho",
                ExistingWorkPolicy.REPLACE,
                workRequest
            )
        }
    }
}