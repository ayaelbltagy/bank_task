package com.example.currency.data.data.api

import com.example.currency.data.data.models.AllCurrenciesResponse
import com.example.currency.data.data.models.HistoryResponse
import com.example.currency.data.data.models.LatestRateResponse
import retrofit2.Response
import retrofit2.http.Path
import javax.inject.Inject

class ApiHelperImpl @Inject constructor(private val apiService: ApiService) : ApiHelper{

    override suspend fun getCurrencies(): Response<AllCurrenciesResponse> = apiService.getAllCurrencies()
    override suspend fun getRates( ): Response<LatestRateResponse> = apiService.getLatestRates( )
    override suspend fun getHistory(date: String): Response<HistoryResponse> = apiService.getHistory(date)

}