package com.francotte.weatherapp.di


import android.content.Context
import com.francotte.weatherapp.data.api.AutoCompleteApi
import com.francotte.weatherapp.data.api.HttpLoggingInterceptor
import com.francotte.weatherapp.data.api.WeatherApi
import com.francotte.weatherapp.domain.WeatherRepositoryImpl
import com.francotte.weatherapp.util.ApplicationScope
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import java.io.File
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun providesCache(@ApplicationContext context: Context): Cache = Cache(File(context.cacheDir, "http_cache"), 10L * 1024 * 1024)

    @Provides
    @Singleton
    fun providesOkHttpClient(cache: Cache): OkHttpClient = OkHttpClient
        .Builder().apply {
            cache(cache)
            addInterceptor(
            HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY)
        )
    }.build()

    @Provides
    @Singleton
    fun providesOpenMeteoApi(okHttpClient: OkHttpClient) : WeatherApi {
        return Retrofit.Builder()
            .baseUrl(OPEN_METEO_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create()
    }


    @Provides
    @Singleton
    fun providesLocationIq(okHttpClient: OkHttpClient) : AutoCompleteApi {
        return Retrofit.Builder()
            .baseUrl(LOCATION_IQ)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create()
    }

    @Provides
    @Singleton
    fun provideWeatherRepo(api: WeatherApi, autoCompleteApi: AutoCompleteApi) : WeatherRepositoryImpl {
        return WeatherRepositoryImpl(api, autoCompleteApi)
    }

}

const val OPEN_METEO_BASE_URL = "https://api.open-meteo.com/v1/"
const val LOCATION_IQ = "https://api.locationiq.com/v1/"
const val LOCATION_IQ_ACCESS_TOKEN = "pk.2b70389e06988dd76200f790facbca1b"

