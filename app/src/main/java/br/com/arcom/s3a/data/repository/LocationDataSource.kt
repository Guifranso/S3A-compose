package br.com.arcom.s3a.data.repository

import br.com.arcom.s3a.network.bodyOrThrow
import br.com.arcom.s3a.network.model.NetworkLocation
import br.com.arcom.s3a.network.services.LocationService
import br.com.arcom.s3a.network.withRetry
import javax.inject.Inject

class LocationDataSource @Inject constructor(
    private val locationService: LocationService
) {

    suspend fun sendLocation(
        latitude: Double,
        longitude: Double,
    ) = withRetry {
        locationService.sendLocation(
            NetworkLocation(
                latitude,
                longitude
            )
        ).bodyOrThrow()
    }

}