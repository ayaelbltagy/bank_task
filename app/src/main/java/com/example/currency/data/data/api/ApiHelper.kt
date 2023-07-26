package com.example.currency.data.data.api

import com.example.currency.data.data.models.AllCurrenciesResponse
import com.example.currency.data.data.models.LatestRateResponse
import retrofit2.Response

interface ApiHelper {

    suspend fun getCurrencies( ): Response<AllCurrenciesResponse>

    suspend fun getRates ( ) :Response<LatestRateResponse>

}