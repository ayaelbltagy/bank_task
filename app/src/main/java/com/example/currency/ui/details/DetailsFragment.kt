package com.example.currency.ui.details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.currency.data.data.viewModel.MainViewModel
import com.example.currency.databinding.FragmentDetailBinding
import com.example.currency.helpers.Status
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONArray
import org.json.JSONObject

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


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        var array=ArrayList<String>()
       // array.add(selectedCurrencyTo)
        latest ("",array)
    }
    @SuppressLint("FragmentLiveDataObserve")
    private fun latest (base:String, array:List<String> ) {
        viewModel.latestRates( ).observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    if (it.data!!.success) {
                        binding.date.text = "Latest rates at" + " " + it.data.date
                        binding.base.text = it.data.base
                        binding.rates.layoutManager = LinearLayoutManager(requireContext())
                        var ratesList = HashMap<String, String>()
                        var gson = Gson()
                        var jsonString = gson.toJson(it.data)
                        val jsonObj = JSONObject(jsonString)
                        val rates = jsonObj.getJSONObject("rates")
                        val map = rates.toMap()
                       // ratesList.
                       binding.rates.adapter= RatesAdapter(map)
                    }
                }
                Status.ERROR -> {}
            }
        }
    }
    fun JSONObject.toMap(): Map<String, *> = keys().asSequence().associateWith {
        when (val value = this[it])
        {
            is JSONArray ->
            {
                val map = (0 until value.length()).associate { Pair(it.toString(), value[it]) }
                JSONObject(map).toMap().values.toList()
            }
            is JSONObject -> value.toMap()
            JSONObject.NULL -> null
            else            -> value
        }
    }
}