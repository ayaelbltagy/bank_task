package com.example.currency.data.data.api

import com.example.currency.data.data.models.AllCurrenciesResponse
import com.example.currency.data.data.models.HistoryResponse
import com.example.currency.data.data.models.LatestRateResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("symbols?access_key=0d1a3e7af52c268e9ad4e1598a0a1053")
    suspend fun getAllCurrencies(): Response<AllCurrenciesResponse>

    @GET("latest?access_key=0d1a3e7af52c268e9ad4e1598a0a1053&&")
    suspend fun getLatestRates(
        @Query("base") base: String,
        @Query("symbols") array: List<String>
    ): Response<LatestRateResponse>

    @GET("{date}?access_key=0d1a3e7af52c268e9ad4e1598a0a1053&&")
    suspend fun getHistory(
        @Path("date") date: String,
        @Query("base") base: String,
        @Query("symbols") array: List<String>
    ): Response<HistoryResponse>

    @GET("convert?access_key=0d1a3e7af52c268e9ad4e1598a0a1053&&&")
    suspend fun convert(
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("amount") amount: String
    )
}