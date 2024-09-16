import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.playlab.workmanagerapp.R
import java.util.concurrent.TimeUnit

class NotificationWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : Worker(
    appContext,
    workerParams
) {

    override fun doWork(): Result {
        showNotification(
            "Trabalho em segundo plano",
            "Tarefa executada!"
        )
        // Agendar novamente a tarefa após 15 minutos
        scheduleWork(applicationContext)
        return Result.success()
    }

    private fun showNotification(
        title: String,
        message: String
    ) {
        val notificationManager =
            applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "canal_id"
        val channelName = "Nome do Canal"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(
            applicationContext,
            channelId
        )
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Substitua pelo ícone do seu aplicativo
            .build()

        notificationManager.notify(
            1,
            notification
        )
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