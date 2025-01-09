package com.francotte.weatherapp.data.model

import kotlinx.serialization.Serializable


@Serializable
data class AutoCompleteResultItem(
    val address: Address? = null,
    val boundingbox: List<String?> = emptyList(),
    val `class`: String? = "",
    val display_address: String? = "",
    val display_name: String? = "",
    val display_place: String? = "",
    val lat: String? = "",
    val licence: String? = "",
    val lon: String? = "",
    val osm_id: String? = "",
    val osm_type: String? = "",
    val place_id: String? = "",
    val type: String? = ""
)

@Serializable
data class Address(
    val country: String? = "",
    val country_code: String? = "",
    val county: String? = "",
    val name: String? = "",
    val postcode: String? = "",
    val state: String?= ""
)

fun AutoCompleteResultItem.asExternalModel():AutoCompleteResult {
   return AutoCompleteResult(place_id.orEmpty(), lat?.toDouble(), lon?.toDouble(), display_place.orEmpty(), display_name.orEmpty())
}

data class AutoCompleteResult(val placeId:String, val latitude:Double?, val longitude:Double?, val shortName:String, val longName:String)