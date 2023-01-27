package br.com.arcom.s3a

import android.telecom.Call
import br.com.arcom.s3a.data.model.LoginRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginAPI {
    @POST("/login")
    fun login(@Body request: LoginRequest): Call
}
