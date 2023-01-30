package br.com.arcom.s3a.data.repository

import android.content.Context
import br.com.arcom.s3a.util.LocationHelper
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationRepository @Inject constructor(
    @ApplicationContext private val context: Context,
    val locationDataSource: LocationDataSource
) {

    suspend fun sendLocation(){
        val location = LocationHelper(context).displayDistance()
        locationDataSource.sendLocation(
            location.latitude,
            location.longitude
        )
    }

}