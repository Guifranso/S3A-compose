package br.com.arcom.s3a.network.services

import br.com.arcom.s3a.network.model.NetworkData
import br.com.arcom.s3a.network.model.NetworkLocation
import br.com.arcom.s3a.network.model.NetworkLogin
import br.com.arcom.s3a.network.model.NetworkTokenPlay
import retrofit2.Response
import retrofit2.http.*


interface CronogramaService {

    @GET("api/s3a/v1/buscar")
    suspend fun buscar(
    ): Response<NetworkData?>
}