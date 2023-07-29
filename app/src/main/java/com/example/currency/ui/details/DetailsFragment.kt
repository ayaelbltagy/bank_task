package com.example.currency.ui.details

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.currency.R
import com.example.currency.data.data.api.Constant
import com.example.currency.data.data.viewModel.MainViewModel
import com.example.currency.databinding.FragmentDetailBinding
import com.example.currency.helpers.Status
import com.example.currency.ui.mainFragment.Companion.selectedCurrencyFrom
import com.example.currency.ui.mainFragment.Companion.selectedCurrencyTo
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.DefaultAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import org.json.JSONArray
import org.json.JSONObject
import java.util.*

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: FragmentDetailBinding
    private lateinit var listOfRatesTo: ArrayList<Entry>
    private lateinit var listOfRatesFrom: ArrayList<Entry>


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
        listOfRatesTo = ArrayList<Entry>()
        listOfRatesFrom = ArrayList<Entry>()

        latest(selectedCurrencyFrom)
        binding.txtCurrencyCodeFrom.setText(selectedCurrencyFrom)
        binding.txtCurrencyCodeTo.setText(selectedCurrencyTo)
        binding.today.setText(Constant.getCurrentDate())
        binding.yesterday.setText(Constant.getLastDay())
        binding.last.setText(Constant.getLast2Days())
        binding.from.setText(selectedCurrencyFrom)
        history(Constant.getCurrentDate(), selectedCurrencyFrom, selectedCurrencyTo)
        history(Constant.getLastDay(), selectedCurrencyFrom, selectedCurrencyTo)
        history(Constant.getLast2Days(), selectedCurrencyFrom, selectedCurrencyTo)
        historyToFrom(Constant.getCurrentDate(), selectedCurrencyTo, selectedCurrencyFrom)
        historyToFrom(Constant.getLastDay(), selectedCurrencyTo, selectedCurrencyFrom)
        historyToFrom(Constant.getLast2Days(), selectedCurrencyTo, selectedCurrencyFrom)


    }

    @SuppressLint("FragmentLiveDataObserve")
    private fun latest(base: String) {
        viewModel.latestRates(base).observe(this) {
            when (it.status) {
                Status.SUCCESS -> {
                    if (it.data!!.success) {
                        binding.date.text = " 10 popular currencies" + " " + it.data.date
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

                        when (it.data.date) {

                            Constant.getCurrentDate() -> {
                                listOfRatesTo.add(Entry(0f, values.get(0).toString().toFloat()))
                                binding.to.setText(selectedCurrencyTo + " " + ":" + " " + values.get(0)
                                )
                              //  setLineChart()

                            }
                            Constant.getLastDay() -> {
                                listOfRatesTo.add(Entry(1f, values.get(0).toString().toFloat()))
                                binding.toYesterday.setText(selectedCurrencyTo + " " + ":" + " " + values.get(0)
                                )
                              setLineChart()

                            }
                            Constant.getLast2Days() -> {
                                listOfRatesTo.add(Entry(2f, values.get(0).toString().toFloat()))
                                binding.toLast.setText(selectedCurrencyTo + " " + ":" + " " + values.get(0)
                                )
                                setLineChart()

                            }
                        }

                    }
                }
                Status.ERROR -> {}
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("FragmentLiveDataObserve")
    private fun historyToFrom(date: String, from: String, to: String) {
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

                        when (it.data.date) {

                            Constant.getCurrentDate() -> {
                                listOfRatesFrom.add(Entry(0f, values.get(0).toString().toFloat()))
                                setLineChart()

                            }
                            Constant.getLastDay() -> {
                                listOfRatesFrom.add(Entry(1f, values.get(0).toString().toFloat()))
                                setLineChart()

                            }
                            Constant.getLast2Days() -> {
                                listOfRatesFrom.add(Entry(2f, values.get(0).toString().toFloat()))
                               // setLineChart()

                            }
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

    @RequiresApi(Build.VERSION_CODES.O)
    private fun setLineChart() {


        binding.lineChart.setDragEnabled(true)
        binding.lineChart.setScaleEnabled(false)
        val dataSets = arrayListOf<ILineDataSet>()

         val lineDataSet = LineDataSet(listOfRatesTo, selectedCurrencyFrom)
        lineDataSet.fillAlpha = 110
        lineDataSet.lineWidth = 2f
        lineDataSet.color = Color.GRAY
        lineDataSet.valueTextSize = 8f
        lineDataSet.valueTextColor = Color.GRAY
        lineDataSet.mode = LineDataSet.Mode.LINEAR
         lineDataSet.setDrawFilled(true)

         val lineDataSet2 = LineDataSet(listOfRatesFrom, selectedCurrencyTo)
        lineDataSet2.fillAlpha = 110
        lineDataSet2.lineWidth = 2f
        lineDataSet2.color = Color.RED
        lineDataSet2.valueTextSize = 8f
        lineDataSet2.valueTextColor = Color.RED
        lineDataSet2.mode = LineDataSet.Mode.LINEAR
       lineDataSet2.setDrawFilled(true)

        dataSets.add(lineDataSet2)
        dataSets.add(lineDataSet)

        //binding.lineChart.axisRight.setEnabled(false)

        val xAxis = binding.lineChart.xAxis
        xAxis.valueFormatter = MyValueFormatter()
        xAxis.isGranularityEnabled = true
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.labelCount = 3
        xAxis.labelRotationAngle = -90f

        binding.lineChart.setDescription(null)
        val lineData = LineData(dataSets)
        lineData.setDrawValues(true)
        binding.lineChart.enableScroll()
        binding.lineChart.isDragEnabled = true
        binding.lineChart.setScaleEnabled(true)
        binding.lineChart.isEnabled = true
        binding.lineChart.data = lineData
        if (listOfRatesFrom.size == 3 && listOfRatesTo.size == 3) {
            binding.lineChart.invalidate()
            Log.e("ListFrom", listOfRatesFrom.get(0).toString())
            Log.e("ListFrom", listOfRatesFrom.get(1).toString())
            Log.e("ListFrom", listOfRatesFrom.get(2).toString())

            Log.e("ListTo", listOfRatesTo.get(0).toString())
            Log.e("ListTo", listOfRatesTo.get(1).toString())
            Log.e("ListTo", listOfRatesTo.get(2).toString())

        }


    }


    class MyValueFormatter() : ValueFormatter() {

        override fun getFormattedValue(value: Float): String {
            return value.toString()
        }

        @RequiresApi(Build.VERSION_CODES.O)
        override fun getAxisLabel(value: Float, axis: AxisBase): String {
            return when (value) {
                0f-> Constant.getCurrentDate()
                1f -> Constant.getLastDay()
                2f -> Constant.getLast2Days()
                else -> ""
            }
        }
    }
    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity?)!!.supportActionBar!!.show()
    }

}
