package com.example.currency.data.data.api

import com.example.currency.data.data.models.AllCurrenciesResponse
import com.example.currency.data.data.models.ConvertResponse
import com.example.currency.data.data.models.HistoryResponse
import com.example.currency.data.data.models.LatestRateResponse
import retrofit2.Response

interface ApiHelper {

    suspend fun getCurrencies( ): Response<AllCurrenciesResponse>
    suspend fun getRates(base:String,array:Array<String>) :Response<LatestRateResponse>
    suspend fun getHistory(date:String, from:String,to:String):Response<HistoryResponse>
    suspend fun convert(from:String,to:String,amount:String):Response<ConvertResponse>
}