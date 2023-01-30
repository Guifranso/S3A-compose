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
//        val tokenPlay = api.updateTokenPlay()
        val tokenPlay = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIyNTs0OzIwMjMtMDEtMzA7MjM3NmZlZWEtY2UxMC00OGY3LWFhMTUtYmUxMDdlOWU1Mzc2IiwiZXhwIjoxNjc3NjAzMDUyfQ.ho6P_-obCIERr1pOFHjvM1xnqyIZcEPXSK9t52TltLk"
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