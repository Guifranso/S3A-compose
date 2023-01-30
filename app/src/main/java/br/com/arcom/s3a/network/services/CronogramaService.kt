package br.com.arcom.s3a.network.services

import br.com.arcom.s3a.network.model.*
import retrofit2.Response
import retrofit2.http.*


interface CronogramaService {

    @GET("api/s3a/v1/buscar")
    suspend fun buscar(
    ): Response<NetworkData?>

    @POST("api/s3a/v1/save")
    suspend fun saveChegagem(
        @Body checagem : NetworkChecagem
    ): Response<Void?>
}