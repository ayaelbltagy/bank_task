package com.example.currency.data.data.models

data class HistoryResponse(
    val success: Boolean,
    val timestamp: Int,
    val historical: Boolean,
    val base: String,
    val date: String,
    val rates: Rates
) {
    data class Rates(
        val USD: Double,
        val CAD: Double,
        val EUR: Int
    )
}