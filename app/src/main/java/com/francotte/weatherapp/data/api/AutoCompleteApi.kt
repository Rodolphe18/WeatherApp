package com.francotte.weatherapp.data.api

import com.francotte.weatherapp.data.model.AutoCompleteResultItem
import com.francotte.weatherapp.di.LOCATION_IQ_ACCESS_TOKEN
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface AutoCompleteApi {

    @GET("autocomplete?tag=place:city,place:town")
    suspend fun getAutoCompleteResult(
        @Query("q") query:String,
        @Query("limit") limit:Int = 10,
        @Query("key") key:String = LOCATION_IQ_ACCESS_TOKEN
    ): Response<List<AutoCompleteResultItem>>

}