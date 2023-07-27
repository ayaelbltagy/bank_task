package com.example.currency.data.data.api

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*


object Constant {
    const val API_KEY = "0d1a3e7af52c268e9ad4e1598a0a1053"

    @RequiresApi(Build.VERSION_CODES.O)
    fun getCurrentDate(): String {
        val current = LocalDateTime.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formattedTime = current.format(formatter)
        return formattedTime
    }

    fun getLastDay(): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val date = Date()
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.DAY_OF_WEEK,1)
        val newDate = calendar.time
        val y: String = formatter.format(newDate)
        return y
    }

    fun getLast2Days(): String {
        val formatter = SimpleDateFormat("yyyy-MM-dd")
        val date = Date()
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.DAY_OF_WEEK,2)
        val newDate = calendar.time
        val y: String = formatter.format(newDate)
        return y
    }

}