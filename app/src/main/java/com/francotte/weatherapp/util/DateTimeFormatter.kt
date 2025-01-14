package com.francotte.weatherapp.util


import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime


object DateTimeFormatter {

    fun getFormattedDate(value: String):String {
        val offSet = LocalDate.parse(value)
        val dayOfMonth = offSet.dayOfMonth
        val month = offSet.month.value
        val formattedDate = "$dayOfMonth/$month"
        return formattedDate
    }

    fun getFormattedTimeForSunsetAndSunrise(value: String):String {
        val time = LocalDateTime.parse(value)
        val minute = if(time.minute < 10) "0${time.minute}" else time.minute
        val hour = time.hour
        val formattedDate = "${hour}h$minute"
        return formattedDate
    }

    fun getFormattedTimeForHourly(value: String):String {
        val nonFormattedHour = LocalDateTime.parse(value).hour
        val formattedHour = if(nonFormattedHour >= 24) nonFormattedHour -24 else nonFormattedHour
        return "$formattedHour h"
    }

}