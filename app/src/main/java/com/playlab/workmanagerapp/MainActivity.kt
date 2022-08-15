package com.playlab.workmanagerapp

import android.os.Bundle
import android.util.Log
import android.widget.Space
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.work.*
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.playlab.workmanagerapp.ui.theme.WorkManagerAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val downloadRequest = OneTimeWorkRequestBuilder<DownloadWorker>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(
                        NetworkType.CONNECTED
                    )
                    .build()
            )
            .build()
        val colorFilterRequest = OneTimeWorkRequestBuilder<ColorFilterWoker>()
            .build()
        val workManager = WorkManager.getInstance(applicationContext)

        setContent {
            WorkManagerAppTheme {
                val workInfos = workManager
                    .getWorkInfosForUniqueWorkLiveData("download")
                    .observeAsState()
                    .value

                val downLoadInfo = remember(key1 = workInfos){
                    workInfos?.find{ it.id == downloadRequest.id}
                }
                val filterInfo = remember(key1 = workInfos){
                    workInfos?.find{ it.id == colorFilterRequest.id}
                }

                val imageUri by remember(key1 = downLoadInfo, key2 = filterInfo) {
                    derivedStateOf {
                        val downloadUri = downLoadInfo?.outputData?.getString(WorkerKeys.IMAGE_URI)
                            ?.toUri()
                        val filterUri = filterInfo?.outputData?.getString(WorkerKeys.FILTER_URI)
                            ?.toUri()
                        filterUri ?: downloadUri
                    }
                }
                Log.d("IMAGEURI", imageUri.toString())
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    imageUri?.let { uri ->
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = uri
                            ),
                            contentDescription = null,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(Modifier.height(16.dp))
                    }
                    Button(onClick = {
                        workManager.beginUniqueWork(
                            "download",
                            ExistingWorkPolicy.KEEP,
                            downloadRequest
                        )
                        .then(colorFilterRequest)
                        .enqueue()
                    },
                    enabled = downLoadInfo?.state != WorkInfo.State.RUNNING
                        ) {
                        Text(text = "Start download")
                    }
                    Spacer(Modifier.height(8.dp))
                    when(downLoadInfo?.state){
                        WorkInfo.State.RUNNING ->  Text(text = "Downloading...")
                        WorkInfo.State.SUCCEEDED ->  Text(text = "Download succeeded")
                        WorkInfo.State.FAILED ->  Text(text = "Download cancelled")
                        WorkInfo.State.ENQUEUED ->  Text(text = "Download enqueed")
                        WorkInfo.State.BLOCKED ->  Text(text = "Download blocked")
                    }
                    Spacer(Modifier.height(8.dp))
                    when(filterInfo?.state){
                        WorkInfo.State.RUNNING ->  Text(text = "Applying filter...")
                        WorkInfo.State.SUCCEEDED ->  Text(text = "Filter succeeded")
                        WorkInfo.State.FAILED ->  Text(text = "Filter cancelled")
                        WorkInfo.State.ENQUEUED ->  Text(text = "Filter enqueed")
                        WorkInfo.State.BLOCKED ->  Text(text = "Filter blocked")
                    }
                }
            }
        }
    }
}
