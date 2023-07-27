package com.example.currency.data.data.viewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currency.data.data.api.Constant
import com.example.currency.data.data.models.AllCurrenciesResponse
import com.example.currency.data.data.models.ConvertResponse
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
import java.text.SimpleDateFormat
import java.util.*
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

    fun latestRates(base:String): LiveData<Resource<LatestRateResponse>> {
        val response = MutableLiveData<Resource<LatestRateResponse>>()
        viewModelScope.launch {
            if (networkHelper.isNetworkConnected()) {
                mainRepository.latestRates(base).let {
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

    fun history(date:String,from:String,to:String): LiveData<Resource<HistoryResponse>> {
        val response = MutableLiveData<Resource<HistoryResponse>>()
        viewModelScope.launch {
            if (networkHelper.isNetworkConnected()) {
                mainRepository.getHistory(date,from,to).let {
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

    fun convert(from: String, to: String, amountValue: String): LiveData<Resource<ConvertResponse>> {
        val response = MutableLiveData<Resource<ConvertResponse>>()
        viewModelScope.launch {
            if (networkHelper.isNetworkConnected()) {
                mainRepository.convert(from,to,amountValue).let {
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

    val time = Calendar.getInstance().time
    val formatter = SimpleDateFormat("yyyy-MM-dd")
    val today = formatter.format(time)



}