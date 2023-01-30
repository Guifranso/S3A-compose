package br.com.arcom.s3a.sync.status

import android.content.Context
import androidx.lifecycle.Transformations
import androidx.lifecycle.asFlow
import androidx.work.WorkInfo
import androidx.work.WorkInfo.State
import androidx.work.WorkManager
import br.com.arcom.s3a.di.SyncStatusMonitor
import br.com.arcom.s3a.sync.initializers.SyncWorkName
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.conflate

/**
 * [SyncStatusMonitor] backed by [WorkInfo] from [WorkManager]
 */
class WorkManagerSyncStatusMonitor @Inject constructor(
    @ApplicationContext context: Context
) : SyncStatusMonitor {
    override val isSyncing: Flow<Boolean> =
        Transformations.map(
            WorkManager.getInstance(context).getWorkInfosForUniqueWorkLiveData(SyncWorkName),
            MutableList<WorkInfo>::anyRunning
        )
            .asFlow()
            .conflate()
}

private val List<WorkInfo>.anyRunning get() = any { it.state == State.RUNNING }
