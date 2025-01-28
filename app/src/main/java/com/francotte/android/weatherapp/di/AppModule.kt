package com.francotte.android.weatherapp.di


import com.francotte.android.weatherapp.data.api.AutoCompleteApi
import com.francotte.android.weatherapp.data.api.HttpLoggingInterceptor
import com.francotte.android.weatherapp.data.api.WeatherApi
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    private val client = OkHttpClient.Builder().apply {
        addInterceptor(
            HttpLoggingInterceptor()
                .setLevel(HttpLoggingInterceptor.Level.BODY)
        )
    }.build()

    @Provides
    @Singleton
    fun providesOpenMeteoApi() : WeatherApi {
        return Retrofit.Builder()
            .baseUrl(OPEN_METEO_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create()
    }


    @Provides
    @Singleton
    fun providesLocationIq() : AutoCompleteApi {
        return Retrofit.Builder()
            .baseUrl(LOCATION_IQ)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create()
    }




}

const val OPEN_METEO_BASE_URL = "https://api.open-meteo.com/v1/"
const val LOCATION_IQ = "https://api.locationiq.com/v1/"
const val LOCATION_IQ_ACCESS_TOKEN = "pk.2b70389e06988dd76200f790facbca1b"

