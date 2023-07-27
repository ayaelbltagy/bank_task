package com.example.currency.data.data.api

import java.text.SimpleDateFormat
import java.util.*


object Constant {
    const val API_KEY = "0d1a3e7af52c268e9ad4e1598a0a1053"

    fun getCurrentDate(): String {
        val time = Calendar.getInstance().time
        val formatter = SimpleDateFormat("YYYY-MM-DD")
        return formatter.format(time)
    }

    fun getLastDay(): String {
        val formatter = SimpleDateFormat("YYYY-MM-DD")
        val date = Date()
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.DAY_OF_WEEK,1)
        val newDate = calendar.time
        val y: String = formatter.format(newDate)
        return y
    }

    fun getLast2Days(): String {
        val formatter = SimpleDateFormat("YYYY-MM-DD")
        val date = Date()
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.DAY_OF_WEEK,2)
        val newDate = calendar.time
        val y: String = formatter.format(newDate)
        return y
    }

}