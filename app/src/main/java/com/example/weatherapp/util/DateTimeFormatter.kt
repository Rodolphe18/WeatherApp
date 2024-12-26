package com.example.weatherapp.util


import kotlinx.datetime.LocalDate

object DateTimeFormatter {

    fun getFormattedDate(value: String):String {
        val offSet = LocalDate.parse(value)
        val dayOfMonth = offSet.dayOfMonth
        val month = offSet.month.value
        val formattedDate = "$dayOfMonth/$month"
        return formattedDate
    }

}