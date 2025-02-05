package com.francotte.weatherapp.util


import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime


object DateTimeFormatter {

    fun getFormattedDate(value: String): String {
        val offSet = LocalDate.parse(value)
        val dayOfMonth = offSet.dayOfMonth
        val day = convertToFrenchDay1(offSet.dayOfWeek.name)
        return "$day $dayOfMonth"
    }

    fun getFormattedDate2(value: String): String {
        val offSet = LocalDate.parse(value)
        val year = offSet.year
        val dayOfMonth = if(offSet.dayOfMonth < 10) "0${offSet.dayOfMonth}" else offSet.dayOfMonth
        val day = convertToFrenchDay2(offSet.dayOfWeek.name)
        val month = convertToFrenchMonth(offSet.month.name)
        return "$day $dayOfMonth $month $year"
    }

    private fun convertToFrenchDay1(englishDay:String):String{
        return when (englishDay) {
            "MONDAY" -> "lun"
            "TUESDAY" -> "mar"
            "WEDNESDAY" -> "mer"
            "THURSDAY" -> "jeu"
            "FRIDAY" -> "ven"
            "SATURDAY" -> "sam"
            "SUNDAY" -> "dim"
            else -> ""
        }
    }

    private fun convertToFrenchDay2(englishDay:String):String{
        return when (englishDay) {
            "MONDAY" -> "lundi"
            "TUESDAY" -> "mardi"
            "WEDNESDAY" -> "mercredi"
            "THURSDAY" -> "jeudi"
            "FRIDAY" -> "vendredi"
            "SATURDAY" -> "samedi"
            "SUNDAY" -> "dimanche"
            else -> ""
        }
    }

    private fun convertToFrenchMonth(englishMonth:String):String{
        return when (englishMonth) {
            "JANUARY" -> "janvier"
            "FEBRUARY" -> "février"
            "MARCH" -> "mars"
            "APRIL" -> "avril"
            "MAY" -> "mai"
            "JUNE" -> "juin"
            "JULY" -> "juillet"
            "AUGUST" -> "août"
            "SEPTEMBER" -> "septembre"
            "OCTOBER" -> "octobre"
            "NOVEMBER" -> "novembre"
            "DECEMBER" -> "décembre"
            else -> ""
        }
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