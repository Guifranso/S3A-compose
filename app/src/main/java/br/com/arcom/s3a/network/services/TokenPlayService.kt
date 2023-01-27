package br.com.arcom.s3a.network.services

import br.com.arcom.s3a.network.model.NetworkLogin
import br.com.arcom.s3a.network.model.NetworkTokenPlay
import retrofit2.Response
import retrofit2.http.*


interface TokenPlayService {

    @POST("api/ivendas/v1/login")
    suspend fun buscarTokenPlay(
        @Body login : NetworkLogin
    ): Response<NetworkTokenPlay>
}