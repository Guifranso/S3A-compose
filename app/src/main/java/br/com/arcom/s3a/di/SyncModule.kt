package br.com.arcom.s3a.di

import br.com.arcom.s3a.sync.status.WorkManagerSyncStatusMonitor
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.flow.Flow

@Module
@InstallIn(SingletonComponent::class)
interface SyncModule {
    @Binds
    fun bindsSyncStatusMonitor(
        syncStatusMonitor: WorkManagerSyncStatusMonitor
    ): SyncStatusMonitor
}

interface SyncStatusMonitor {
    val isSyncing: Flow<Boolean>
}