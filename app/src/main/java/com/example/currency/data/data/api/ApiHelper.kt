package com.example.currency.data.data.api

import com.example.currency.data.data.models.AllCurrenciesResponse
import com.example.currency.data.data.models.HistoryResponse
import com.example.currency.data.data.models.LatestRateResponse
import retrofit2.Response

interface ApiHelper {

    suspend fun getCurrencies( ): Response<AllCurrenciesResponse>
    suspend fun getRates(base:String,array:List<String>) :Response<LatestRateResponse>
    suspend fun getHistory(date:String, base:String,array:List<String>):Response<HistoryResponse>

}