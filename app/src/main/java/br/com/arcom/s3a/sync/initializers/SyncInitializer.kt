package br.com.arcom.s3a.sync.initializers

import android.content.Context
import androidx.startup.AppInitializer
import androidx.startup.Initializer
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.WorkManager
import androidx.work.WorkManagerInitializer
import br.com.arcom.s3a.sync.workers.SyncLastLocation
import br.com.arcom.s3a.sync.workers.SyncUltimaVersaoApp

object Sync {
    // This method is a workaround to manually initialize the sync process instead of relying on
    // automatic initialization with Androidx Startup. It is called from the app module's
    // Application.onCreate() and should be only done once.
    fun initialize(context: Context) {
        AppInitializer.getInstance(context)
            .initializeComponent(SyncInitializer::class.java)
    }
}

// This name should not be changed otherwise the app may have concurrent sync requests running
internal const val SyncWorkName = "SyncWorkName"
internal const val SyncWorkNameSendLocation = "SyncWorkNameSendLocation"
internal const val SyncWorkNameUpdateVersion = "SyncWorkNameUpdateVersion"

/**
 * Registers work to sync the data layer periodically on app startup.
 */
class SyncInitializer : Initializer<Sync> {
    override fun create(context: Context): Sync {
        WorkManager.getInstance(context).apply {
            // Run sync on app startup and ensure only one sync worker runs at any time
            enqueueUniquePeriodicWork(
                SyncWorkNameSendLocation,
                ExistingPeriodicWorkPolicy.REPLACE,
                SyncLastLocation.startUpSyncWork()
            )
        }
        WorkManager.getInstance(context).apply {
            // Run sync on app startup and ensure only one sync worker runs at any time
            enqueueUniquePeriodicWork(
                SyncWorkNameUpdateVersion,
                ExistingPeriodicWorkPolicy.REPLACE,
                SyncUltimaVersaoApp.startUpSyncWork()
            )
        }
        return Sync
    }

    override fun dependencies(): List<Class<out Initializer<*>>> =
        listOf(WorkManagerInitializer::class.java)
}
