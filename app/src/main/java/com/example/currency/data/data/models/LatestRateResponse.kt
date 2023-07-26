package com.example.currency.data.data.models

data class LatestRateResponse(
    val success: Boolean,
    val timestamp: Int,
    val base: String,
    val date: String,
    val rates: Rates
) {
    data class Rates(
        val AED: Double,
        val AFN: Double,
        val ALL: Double,
        val AMD: Double,
        val ANG: Double,
        val AOA: Double,
        val ARS: Double,
        val AUD: Double,
        val AWG: Double,
        val AZN: Double,
        val BAM: Double,
        val BYN: Double,
        val BYR: Double,
        val MAD: Double,
        val MDL: Double,
        val MGA: Double,
        val MKD: Double,
        val MMK: Double,
        val MNT: Double,
        val MOP: Double,
        val MRO: Double,
        val MUR: Double,
        val MVR: Double,
        val MWK: Double,
        val MXN: Double,
        val MYR: Double,
        val MZN: Double,
        val NAD: Double,
        val NGN: Double,
        val NIO: Double,
        val NOK: Double,


    )
}