package br.com.arcom.s3a.sync.workers

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkerParameters
import br.com.arcom.s3a.auth.AuthManager
import br.com.arcom.s3a.sync.initializers.SyncConstraints
import br.com.arcom.s3a.sync.initializers.syncForegroundInfo
import br.com.arcom.s3a.util.Logger
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.DelicateCoroutinesApi
import java.util.concurrent.TimeUnit

@HiltWorker
class SyncUltimaVersaoApp @AssistedInject constructor(
    @Assisted private val appContext: Context,
    @Assisted params: WorkerParameters,
//    private val atualizarVersaoApp: AtualizarVersaoApp,
//    private val atualizarPushToken: AtualizarPushToken,
    private val authManager: AuthManager,
    private val logger: Logger
) : CoroutineWorker(appContext, params) {

    override suspend fun getForegroundInfo(): ForegroundInfo =
        appContext.syncForegroundInfo()

    @OptIn(DelicateCoroutinesApi::class)
    override suspend fun doWork(): Result {
        try {
            val idSetor = authManager.authState.value.idSetor
            if ( idSetor != null ) {
//                atualizarVersaoApp.executeSync(AtualizarVersaoApp.Params(idSetor = idSetor.toShort(), versaoAtual = BuildConfig.VERSION_NAME))
//                atualizarPushToken.executeSync(AtualizarPushToken.Params())
            }
        } catch (ex: Throwable) {
            // nao faz nada
        }
        return Result.success()
    }


    companion object {
        fun startUpSyncWork() = PeriodicWorkRequestBuilder<DelegatingWorker>(15, TimeUnit.MINUTES)
            .setConstraints(SyncConstraints)
            .setInputData(SyncUltimaVersaoApp::class.delegatedData())
            .build()
    }
}