package com.example.currency.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.Navigation
import com.example.currency.R
import com.example.currency.data.data.models.AllCurrenciesResponse
import com.example.currency.data.data.viewModel.MainViewModel
import com.example.currency.databinding.FragmentMainBinding
import com.example.currency.helpers.Status.*
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONArray
import org.json.JSONObject


@AndroidEntryPoint
class mainFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: FragmentMainBinding

    companion object{
        var selectedCurrencyFrom = ""
        var selectedCurrencyTo= ""
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)
        getCurrencies()
        binding.spinnerFrom.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newText ->
            selectedCurrencyFrom = newText
            binding.spinnerFrom.dismiss()
        }
        binding.spinnerTo.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newText ->
            selectedCurrencyTo = newText
            binding.spinnerTo.dismiss()
        }
        binding.swap.setOnClickListener {
            binding.spinnerTo.dismiss()
            binding.spinnerFrom.dismiss()
            change()
        }
        binding.details.setOnClickListener {
            binding.spinnerTo.dismiss()
            binding.spinnerFrom.dismiss()
            Navigation.findNavController(it).navigate(R.id.mainFragment_to_detailsFragment)
        }
    }

    private fun change(){

    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun getCurrencies() {
        viewModel.currencies().observe(this) {
            when (it.status) {
                SUCCESS -> {
                    if (it.data!!.success) {
                        var gson = Gson()
                        var jsonString = gson.toJson(it.data)
                        val jsonObj = JSONObject(jsonString)
                        val symbols = jsonObj.getJSONObject("symbols")
                        val listCurrency = ArrayList<String>()
                        val keys = symbols.keys()
                        while (keys.hasNext()) {
                            val key = keys.next()
                            listCurrency.add(key)
                        }

//                        val x: Iterator<String> = symbols.keys()
//                        val jsonArray = JSONArray()
//                        while (x.hasNext()) {
//                            val key = x.next()
//                            jsonArray.put(symbols[key])
//                        }

//                        val listdata = ArrayList<String>()
//                        if (jsonArray != null) {
//                            for (i in 0 until jsonArray.length()) {
//                                listdata.add(jsonArray.getString(i))
//
                               binding.spinnerFrom.setItems(listCurrency)
                        binding.spinnerTo.setItems(listCurrency)
//                            }
                      //  }
                    }
                }
                ERROR -> {}
            }
        }
    }


}