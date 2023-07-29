package com.example.currency.ui

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
    private var isConvertButtonClicked = false

    companion object {
        var selectedCurrencyFrom = ""
        var selectedCurrencyTo = ""
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
            isConvertButtonClicked = false


        }
        binding.spinnerTo.setOnSpinnerItemSelectedListener<String> { oldIndex, oldItem, newIndex, newText ->
            selectedCurrencyTo = newText
            binding.spinnerTo.dismiss()
            isConvertButtonClicked = false
            change(binding.input.text.toString())

        }
        binding.swap.setOnClickListener {
            isConvertButtonClicked = true
            binding.spinnerTo.dismiss()
            binding.spinnerFrom.dismiss()
            change(binding.output.text.toString())
        }
        binding.details.setOnClickListener {
            isConvertButtonClicked = false
            binding.spinnerTo.dismiss()
            binding.spinnerFrom.dismiss()
            if(selectedCurrencyFrom.equals("") ||selectedCurrencyTo.equals("")){
                Toast.makeText(requireContext(),"Please select from - to currencies",Toast.LENGTH_LONG).show()
            }
            else{
                Navigation.findNavController(it).navigate(R.id.mainFragment_to_detailsFragment)

            }
        }
    }

    @SuppressLint("FragmentLiveDataObserve")

    private fun change(amount: String) {
        viewModel.convert(selectedCurrencyFrom, selectedCurrencyTo, amount).observe(this) {
            when (it.status) {
                SUCCESS -> {
                    if (it.data!!.success) {
                        if(isConvertButtonClicked){
                            binding.input.setText(it.data.result.toString())
                        }
                        else{
                            binding.output.setText(it.data.result.toString())
                        }
                        isConvertButtonClicked = false
                    }
                }
                ERROR -> {}
            }
        }

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
                        binding.spinnerFrom.setItems(listCurrency)
                        binding.spinnerTo.setItems(listCurrency)

                    }
                }
                ERROR -> {}
            }
        }
    }

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()

        if (view == null) {
            return
        }
        requireView().isFocusableInTouchMode = true
        requireView().requestFocus()
        requireView().setOnKeyListener(object : View.OnKeyListener {
            override fun onKey(v: View?, keyCode: Int, event: KeyEvent): Boolean {
                return if (event.getAction() === KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK) {
                    requireActivity().finish()
                    true
                } else false
            }
        })
    }
}


