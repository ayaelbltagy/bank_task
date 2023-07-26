package com.example.currency.data.data.models

data class ConvertResponse(
    val success: Boolean,
    val query: Query,
    val info: Info,
    val date: String,
    val result: Double
) {
    data class Query(
        val from: String,
        val to: String,
        val amount: Int
    )

    data class Info(
        val timestamp: Int,
        val rate: Double
    )
}