package br.com.arcom.s3a.network

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Base64
import br.com.arcom.s3a.network.model.NetworkLogin
import br.com.arcom.s3a.network.model.NetworkTokenPlay
import br.com.arcom.s3a.network.services.*
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import okio.*
import retrofit2.Retrofit
import java.nio.charset.StandardCharsets
import java.util.concurrent.TimeUnit


class NetworkS3aApi(
    val apiBaseUrl: String,
    val context: Context,
    val tokenPlayPrefs: SharedPreferences,
    val json: Json
) {

    private var retrofit: Retrofit? = null

    private var tokenHeaderArcomId: String? = null

    private var tokenArcomId: String? = null

    private var setorS3a: Long? = null

    private var tokenPlay: String? = null

    @OptIn(ExperimentalSerializationApi::class)
    protected fun retrofit(): Retrofit {
        if (retrofit == null) {
            retrofit = Retrofit.Builder()
                .baseUrl(apiBaseUrl)
                .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
                .client(OkHttpClient.Builder()
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .readTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS)
                    .authenticator(S3AAuthenticator(this))
                    .addInterceptor(S3AInterceptor(this))
                    .addInterceptor(HttpLoggingInterceptor().apply {
                        setLevel(HttpLoggingInterceptor.Level.BODY)
                    })
                    .addNetworkInterceptor {
                        val originalResponse = it.proceed(it.request())
                        val request = it.request()
                        val progressCallback = request.tag(ProgressListener::class.java)
                        if (progressCallback != null) {
                            return@addNetworkInterceptor originalResponse.newBuilder()
                                .body(ProgressResponseBody(originalResponse.body, progressCallback))
                                .build()
                        }
                        return@addNetworkInterceptor originalResponse
                    }.build()
                ).build()
        }
        return retrofit!!
    }

    fun atualizarTokenPlay(token: String) {
        tokenPlay = token
    }

    fun atualizarSetor(setor: Long) {
        setorS3a = setor
    }

    fun getTokenPlay(): String {
        return tokenPlay ?: "0"
    }

    fun getToken(): String {
        if (tokenHeaderArcomId == null) {
            tokenHeaderArcomId = updateToken()
        }
        return tokenHeaderArcomId!!
    }

    @SuppressLint("NewApi")
    fun updateToken(): String {
        val cursor = context.contentResolver.query(
            Uri.parse("content://br.com.arcom.id.provider/token"),
            null,
            null,
            null,
            null
        )
        if (cursor != null) {
            try {
                if (cursor.moveToFirst()) {
                    tokenArcomId = cursor.getString(0)
                    tokenHeaderArcomId = Base64.encodeToString(
                        (cursor.getString(0) + ":").toByteArray(
                            StandardCharsets.ISO_8859_1
                        ), Base64.NO_WRAP
                    )
                    return tokenHeaderArcomId!!
                }
            } finally {
                if (!cursor.isClosed) cursor.close()
            }
        }
        throw Exception("Token Obrigatório para conexao!")
    }

    fun appTokenPlayService(): TokenPlayService {
        return retrofit().create(TokenPlayService::class.java)
    }

    fun updateTokenPlay(): String? {
        return runBlocking {
            try {
                if (setorS3a != null && tokenArcomId != null) {
                    val tokenResponse = getUpdatedToken()
                    tokenPlayPrefs.edit().putString("tokenPlay", tokenResponse.token).commit()
                    tokenPlay = tokenResponse.token
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
            tokenPlay
        }
    }

    private suspend fun getUpdatedToken(): NetworkTokenPlay {
        return withContext(Dispatchers.IO) {
            appTokenPlayService().buscarTokenPlay(NetworkLogin(setorS3a!!, tokenArcomId!!)).bodyOrThrow()!!
        }
    }

    private class ProgressResponseBody internal constructor(
        private val responseBody: ResponseBody,
        private val progressListener: ProgressListener
    ) : ResponseBody() {

        private var bufferedSource: BufferedSource? = null
        override fun contentType(): MediaType? {
            return responseBody.contentType()
        }

        override fun contentLength(): Long {
            return responseBody.contentLength()
        }

        override fun source(): BufferedSource {
            if (bufferedSource == null) {
                bufferedSource = source(responseBody.source()).buffer()
            }
            return bufferedSource!!
        }

        private fun source(source: Source): Source {
            return object : ForwardingSource(source) {
                var totalBytesRead = 0L

                override fun read(sink: Buffer, byteCount: Long): Long {
                    val bytesRead = super.read(sink, byteCount)
                    // read() returns the number of bytes read, or -1 if this source is exhausted.
                    totalBytesRead += if (bytesRead != -1L) bytesRead else 0
                    progressListener.update(
                        totalBytesRead,
                        responseBody.contentLength(),
                        bytesRead == -1L
                    )
                    return bytesRead
                }
            }
        }
    }

    interface ProgressListener {
        fun update(bytesRead: Long, contentLength: Long, done: Boolean)
    }
}