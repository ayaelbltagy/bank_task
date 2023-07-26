package com.example.currency.data.data.api

import com.example.currency.data.data.models.AllCurrenciesResponse
import com.example.currency.data.data.models.ConvertResponse
import com.example.currency.data.data.models.HistoryResponse
import com.example.currency.data.data.models.LatestRateResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("symbols?access_key=${Constant.API_KEY}")
    suspend fun getAllCurrencies(): Response<AllCurrenciesResponse>

    @GET("latest?access_key=${Constant.API_KEY}&symbols=EGP,SGD,CHF,CAD,AUD,CNY,GBP,JPY,EUR,USD")
    suspend fun getLatestRates(
        @Query("base") base: String
    ): Response<LatestRateResponse>

    @GET("{date}?access_key=${Constant.API_KEY}")
    suspend fun getHistory(
        @Path("date") date: String,
        @Query("base") base: String,
        @Query("symbols") array:  String
    ): Response<HistoryResponse>

    @GET("convert?access_key=${Constant.API_KEY}")
    suspend fun convert(
        @Query("from") from: String,
        @Query("to") to: String,
        @Query("amount") amount: String
    ):Response<ConvertResponse>
}