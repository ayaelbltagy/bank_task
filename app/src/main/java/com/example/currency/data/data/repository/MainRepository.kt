package com.example.currency.data.data.repository

import com.example.currency.data.data.api.ApiHelper
import com.example.currency.data.data.models.AllCurrenciesResponse
import com.example.currency.data.data.models.HistoryResponse
import com.example.currency.data.data.models.LatestRateResponse
import retrofit2.Response
import retrofit2.http.Query
import javax.inject.Inject

class MainRepository @Inject constructor(private val apiHelper: ApiHelper) {

    suspend fun allCurrencies():Response<AllCurrenciesResponse> = apiHelper.getCurrencies()
    suspend fun latestRates(base:String,array:List<String>) :Response<LatestRateResponse> = apiHelper.getRates(base,array)
    suspend fun getHistory(date: String, base:String, array:List<String>):Response<HistoryResponse> = apiHelper.getHistory(date,base,array)
}