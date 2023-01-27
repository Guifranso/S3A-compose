package br.com.arcom.s3a.network

import okhttp3.*
import java.lang.RuntimeException


class S3AAuthenticator(val api: NetworkS3aApi) : Authenticator {

    override fun authenticate(route: Route?, response: Response): Request {
        if (responseCount(response) >= 2) {
            throw RuntimeException(
                "Authentication failed: You do not have permissions to access the service."
            )
        }
        val token = api.updateToken()
        val tokenPlay = api.updateTokenPlay()
        return response.request.newBuilder()
            .header("Authorization", "Basic $token,Bearer $tokenPlay").build()
    }

    private fun responseCount(response: Response): Int {
        var result = 1
        while (response.priorResponse != null) {
            result++
        }
        return result
    }
}