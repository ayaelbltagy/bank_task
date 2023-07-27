package com.example.currency.ui.details

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.currency.data.data.api.ApiService
import com.example.currency.data.data.api.Constant
import com.example.currency.data.data.viewModel.MainViewModel
import com.example.currency.databinding.FragmentDetailBinding
import com.example.currency.helpers.Status
import com.example.currency.ui.mainFragment.Companion.selectedCurrencyFrom
import com.example.currency.ui.mainFragment.Companion.selectedCurrencyTo
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: FragmentDetailBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        return binding.root

    }


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        latest(selectedCurrencyFrom)
        binding.today.setText(Constant.getCurrentDate())
        binding.yesterday.setText(Constant.getLastDay())
        binding.last.setText(Constant.getLast2Days())
        binding.from.setText(selectedCurrencyFrom)
        history (Constant.getCurrentDate(), selectedCurrencyFrom, selectedCurrencyTo)
        history (Constant.getLastDay(), selectedCurrencyFrom, selectedCurrencyTo)
        history (Constant.getLast2Days(), selectedCurrencyFrom, selectedCurrencyTo)

    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun latest(base: String) {
        viewModel.latestRates(base).observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    if (it.data!!.success) {
                        binding.date.text = "Latest rates at" + " " + it.data.date
                        binding.base.text = it.data.base
                        binding.rates.layoutManager = LinearLayoutManager(requireContext())
                        var gson = Gson()
                        var jsonString = gson.toJson(it.data)
                        val jsonObj = JSONObject(jsonString)
                        val rates = jsonObj.getJSONObject("rates")
                        val map = rates.toMap()
                        binding.rates.adapter = RatesAdapter(map)
                    }
                }
                Status.ERROR -> {}
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("FragmentLiveDataObserve")
    private fun history(date: String, from: String, to: String) {
        viewModel.history(date, from, to).observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    if (it.data!!.success) {
                        var gson = Gson()
                        var jsonString = gson.toJson(it.data)
                        val jsonObj = JSONObject(jsonString)
                        val rates = jsonObj.getJSONObject("rates")
                        val map = rates.toMap()
                        var values = map.values.toList()
                        when(it.data.date){
                            Constant.getCurrentDate()->{ binding.to.setText(selectedCurrencyTo + " "+":" +" "+ values.get(0)) }
                            Constant.getLastDay()->{binding.toYesterday.setText(selectedCurrencyTo + " "+":" +" "+ values.get(0))}
                            Constant.getLast2Days()->{binding.toLast.setText(selectedCurrencyTo + " "+":" +" "+ values.get(0))}
                        }
                    }
                }
                Status.ERROR -> {}
            }
        }
    }

    fun JSONObject.toMap(): Map<String, *> = keys().asSequence().associateWith {
        when (val value = this[it]) {
            is JSONArray -> {
                val map =
                    (0 until value.length()).associate { Pair(it.toString(), value[it]) }
                JSONObject(map).toMap().values.toList()
            }
            is JSONObject -> value.toMap()
            JSONObject.NULL -> null
            else -> value
        }
    }

}
