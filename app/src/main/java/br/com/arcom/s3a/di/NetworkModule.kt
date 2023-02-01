package br.com.arcom.s3a.di

import android.content.Context
import android.content.SharedPreferences
import br.com.arcom.s3a.network.NetworkS3aApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import javax.inject.Named
import javax.inject.Singleton

const val BASE_URL = "https://1a0e-200-251-86-133.sa.ngrok.io"

@InstallIn(SingletonComponent::class)
@Module(includes = [ApiServiceModule::class])
object NetworkRetrofitRepModule {

    @Provides
    @Singleton
    fun provideNetworkS3aApi(
        @ApplicationContext context: Context,
        @Named("tokenPlay") tokenPlayPrefs: SharedPreferences,
        json: Json
    ): NetworkS3aApi = NetworkS3aApi(BASE_URL, context, tokenPlayPrefs, json)

    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

}

@InstallIn(SingletonComponent::class)
@Module
object ApiServiceModule {

    @Provides
    fun provideTokenPlayService(networkRepApi: NetworkS3aApi) = networkRepApi.appTokenPlayService()

    @Provides
    fun provideLocationService(networkRepApi: NetworkS3aApi) = networkRepApi.appLocationService()

    @Provides
    fun provideCronogramaService(networkRepApi: NetworkS3aApi) = networkRepApi.appCronogramaService()
}