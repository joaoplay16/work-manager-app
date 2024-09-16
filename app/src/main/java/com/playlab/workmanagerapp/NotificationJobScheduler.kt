import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import com.playlab.workmanagerapp.NotificationJobService

class NotificationJobScheduler {

    companion object {
        fun scheduleNotification(context: Context, intervalMillis: Long) {
            val jobScheduler =
                context.getSystemService(Context.JOB_SCHEDULER_SERVICE) as JobScheduler
            val jobInfo = JobInfo.Builder(
                1,
                ComponentName(
                    context,
                    NotificationJobService::class.java
                )
            )
                .setPeriodic(intervalMillis) // 15 minutos em milissegundos
                .build()

            jobScheduler.schedule(jobInfo)
        }
    }
}

