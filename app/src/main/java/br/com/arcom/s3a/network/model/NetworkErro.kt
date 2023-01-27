package br.com.arcom.s3a.network.model

import com.google.gson.Gson
import retrofit2.HttpException

data class NetworkPlayError(
    val erro: NetworkErro
)

data class NetworkErro (
    val message: String,
    val stack: String
)

fun HttpException.asNetworkPlayError() = Gson().fromJson(this.response()?.errorBody()?.string(), NetworkPlayError::class.java).erro.message
