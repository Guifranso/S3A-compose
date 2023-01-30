package br.com.arcom.s3a.sync.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkerParameters
import br.com.arcom.s3a.data.domain.SendLocation
import br.com.arcom.s3a.sync.initializers.SyncConstraints
import br.com.arcom.s3a.sync.initializers.syncForegroundInfo
import br.com.arcom.s3a.util.Logger
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.DelicateCoroutinesApi
import java.util.concurrent.TimeUnit

@HiltWorker
class SyncLastLocation @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted params: WorkerParameters,
    private val sendLocation: SendLocation,
    private val logger: Logger
) : CoroutineWorker(appContext, params) {

    override suspend fun getForegroundInfo(): ForegroundInfo =
        appContext.syncForegroundInfo()

    override suspend fun doWork(): Result {
        try {
            sendLocation.executeSync(SendLocation.Params())
        } catch (ex: Throwable) {
            // nao faz nada
        }
        return Result.success()
    }


    companion object {
        fun startUpSyncWork() = PeriodicWorkRequestBuilder<DelegatingWorker>(4, TimeUnit.HOURS)
            .setConstraints(SyncConstraints)
            .setInputData(SyncLastLocation::class.delegatedData())
            .build()
    }
}