package com.example.currency.data.data.api

import com.example.currency.data.data.models.*
import retrofit2.Response
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(private val apiService: ApiService) : ApiHelper{

    override suspend fun getCurrencies(): Response<AllCurrenciesResponse> = apiService.getAllCurrencies()
    override suspend fun getRates(base:String): Response<LatestRateResponse> = apiService.getLatestRates(base)
    override suspend fun getHistory(date: String, from:String, to:String): Response<HistoryResponse> = apiService.getHistory(date,from,to)
    override suspend fun convert(from: String, to: String, amountValue: String): Response<ConvertResponse> = apiService.convert(from,to,amountValue)


}