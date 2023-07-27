package com.example.currency.data.data.repository

import com.example.currency.data.data.api.ApiHelper
import com.example.currency.data.data.models.*
import retrofit2.Response
import javax.inject.Inject

class MainRepository @Inject constructor(private val apiHelper: ApiHelper) {
    suspend fun allCurrencies():Response<AllCurrenciesResponse> = apiHelper.getCurrencies()
    suspend fun latestRates(base:String) :Response<LatestRateResponse> = apiHelper.getRates(base)
    suspend fun getHistory(date: String, from:String, to:String):Response<HistoryResponse> = apiHelper.getHistory(date,from,to)
    suspend fun convert(from: String, to: String, amountValue: String):Response<ConvertResponse> = apiHelper.convert(from,to,amountValue)
}