package com.example.currency.data.data.api

import com.example.currency.data.data.models.*
import retrofit2.Response

interface ApiHelper {

    suspend fun getCurrencies(): Response<AllCurrenciesResponse>
    suspend fun getRates(base:String) :Response<LatestRateResponse>
    suspend fun getHistory(date: String, from:String, to:String):Response<HistoryResponse>
    suspend fun convert(from:String,to:String,amount:String):Response<ConvertResponse>


}