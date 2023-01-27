package br.com.arcom.s3a.network

import okhttp3.HttpUrl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class S3AInterceptor(val api: NetworkS3aApi) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request: Request = chain.request()

        // add (or replace) the API key query parameter
        val urlBuilder: HttpUrl.Builder = request.url.newBuilder()

        val builder: Request.Builder =
            request.newBuilder()
                .addHeader("Authorization", "Basic ${api.getToken()},Bearer ${api.getTokenPlay()}").url(urlBuilder.build())

        return chain.proceed(builder.build())
    }

}