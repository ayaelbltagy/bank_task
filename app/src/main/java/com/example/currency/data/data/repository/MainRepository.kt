package com.example.currency.data.data.repository

import com.example.currency.data.data.api.ApiHelper
import com.example.currency.data.data.models.AllCurrenciesResponse
import com.example.currency.data.data.models.HistoryResponse
import com.example.currency.data.data.models.LatestRateResponse
import retrofit2.Response
import javax.inject.Inject

class MainRepository @Inject constructor(private val apiHelper: ApiHelper) {

    suspend fun allCurrencies():Response<AllCurrenciesResponse> = apiHelper.getCurrencies()
    suspend fun latestRates() :Response<LatestRateResponse> = apiHelper.getRates()
    suspend fun getHistory(date: String):Response<HistoryResponse> = apiHelper.getHistory(date)
}