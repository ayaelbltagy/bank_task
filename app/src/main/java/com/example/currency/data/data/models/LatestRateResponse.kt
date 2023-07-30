package com.example.currency.data.data.models

data class LatestRateResponse(
    val success: Boolean,
    val timestamp: Int,
    val base: String,
    val date: String,
    val rates: Rates
) {
    data class Rates(

        val USD: Double,
        val EUR: Double,
        val JPY: Double,
        val GBP: Double,
        val CNY: Double,
        val AUD: Double,
        val CAD: Double,
        val CHF: Double,
        val SGD: Double,
        val EGP: Double

    )
}