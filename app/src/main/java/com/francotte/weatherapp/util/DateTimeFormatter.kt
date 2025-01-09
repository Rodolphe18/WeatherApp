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

    fun getFormattedTimeForHourly(value: String, offSetSeconds:Int?):String {
        val offSetInHour = (offSetSeconds?.div(3600) ?: 0)
        val nonFormattedHour = LocalDateTime.parse(value).hour
       // val hour = if(abs(offSetInHour) > abs(nonFormattedHour) && offSetInHour < 0) nonFormattedHour+offSetInHour+24 else nonFormattedHour + offSetInHour
        val formattedHour = if(nonFormattedHour >= 24) nonFormattedHour -24 else nonFormattedHour
        val formattedDate = "$formattedHour h"
        return formattedDate
    }

}