package com.app.user.utils

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat.startActivity
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object FunUtil {
    @RequiresApi(Build.VERSION_CODES.O)
    fun reformatDate(dateString: String): String = formatDateString(dateString)


    @RequiresApi(Build.VERSION_CODES.O)
    private fun formatDateString(date: String): String {
        val dateTimeFormatter = DateTimeFormatter.ISO_DATE_TIME
        val localDateTime = LocalDateTime.parse(date, dateTimeFormatter)
        val dateFormatter = DateTimeFormatter.ISO_DATE
        return localDateTime.format(dateFormatter)
    }

    fun getTimeFromDateString(date: String): String {
        val timeStartIndex = date.indexOf("T") + 1
        return date.substring(timeStartIndex, date.length)
    }

}