package com.example.currency.data.data.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currency.data.data.models.AllCurrenciesResponse
import com.example.currency.data.data.models.HistoryResponse
import com.example.currency.data.data.models.LatestRateResponse
import com.example.currency.data.data.repository.MainRepository
import com.example.currency.helpers.NetworkHelper
import com.example.currency.helpers.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.http.Query
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
     private val mainRepository: MainRepository,
     private val networkHelper: NetworkHelper) : ViewModel() {

     fun currencies( ): LiveData<Resource<AllCurrenciesResponse>> {
        val response = MutableLiveData<Resource<AllCurrenciesResponse>>()
        viewModelScope.launch {
            if (networkHelper.isNetworkConnected()) {
                mainRepository.allCurrencies( ).let {
                    if (it.isSuccessful) {
                        response.postValue(Resource.success(it.body(), ""))
                      } else {
                        val jsonObj = JSONObject(it.errorBody()!!.charStream().readText())
                        response.postValue(Resource.error(jsonObj["message"].toString(), null))
                     }
                }
            } else response.postValue(Resource.error("No internet connection", null))
        }
        return response
    }

    fun latestRates(base:String,array:List<String>): LiveData<Resource<LatestRateResponse>> {
        val response = MutableLiveData<Resource<LatestRateResponse>>()
        viewModelScope.launch {
            if (networkHelper.isNetworkConnected()) {
                mainRepository.latestRates(base,array).let {
                    if (it.isSuccessful) {
                        response.postValue(Resource.success(it.body(), ""))
                    } else {
                        val jsonObj = JSONObject(it.errorBody()!!.charStream().readText())
                        response.postValue(Resource.error(jsonObj["message"].toString(), null))
                    }
                }
            } else response.postValue(Resource.error("No internet connection", null))
        }
        return response
    }
    fun history(date:String,base:String,array:List<String>): LiveData<Resource<HistoryResponse>> {
        val response = MutableLiveData<Resource<HistoryResponse>>()
        viewModelScope.launch {
            if (networkHelper.isNetworkConnected()) {
                mainRepository.getHistory(date,base,array).let {
                    if (it.isSuccessful) {
                        response.postValue(Resource.success(it.body(), ""))
                    } else {
                        val jsonObj = JSONObject(it.errorBody()!!.charStream().readText())
                        response.postValue(Resource.error(jsonObj["message"].toString(), null))
                    }
                }
            } else response.postValue(Resource.error("No internet connection", null))
        }
        return response
    }
}