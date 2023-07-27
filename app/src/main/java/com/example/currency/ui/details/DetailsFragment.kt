package com.example.currency.ui.details

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
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
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
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
         binding.txtCurrencyCodeFrom.setText(selectedCurrencyFrom)
        binding.txtCurrencyCodeTo.setText(selectedCurrencyTo)
        binding.today.setText(Constant.getCurrentDate())
        binding.yesterday.setText(Constant.getLastDay())
        binding.last.setText(Constant.getLast2Days())
        binding.from.setText(selectedCurrencyFrom)
        history(Constant.getCurrentDate(), selectedCurrencyFrom, selectedCurrencyTo)
        history(Constant.getLastDay(), selectedCurrencyFrom, selectedCurrencyTo)
        history(Constant.getLast2Days(), selectedCurrencyFrom, selectedCurrencyTo)

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
                        val listOfRatesTo = arrayListOf<Entry>()

                        when (it.data.date) {

                            Constant.getCurrentDate() -> {
                                binding.to.setText(selectedCurrencyTo + " " + ":" + " " + values.get(0))
                                listOfRatesTo.add(Entry(0f, values.get(0).toString().toFloat()))
                            }
                            Constant.getLastDay() -> {
                                binding.toYesterday.setText(
                                    selectedCurrencyTo + " " + ":" + " " + values.get(0))
                                listOfRatesTo.add(Entry(1f,  values.get(0).toString().toFloat()))
                            }
                            Constant.getLast2Days() -> {
                                binding.toLast.setText(
                                    selectedCurrencyTo + " " + ":" + " " + values.get(0))
                                listOfRatesTo.add(Entry(2f,  values.get(0).toString().toFloat()))
                            }
                        }
                        val dates = arrayListOf<String>()
                        dates.add(Constant.getCurrentDate())
                        dates.add(Constant.getLastDay())
                        dates.add(Constant.getLast2Days())

                        setLineChart(dates, listOfRatesTo)
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

    private fun setLineChart(
        dates: List<String>,
        listOfRatesTo: ArrayList<Entry>
    ) {
        binding.lineChart.setDragEnabled(true)
        binding.lineChart.setScaleEnabled(false)

        val lineDataSet = LineDataSet(listOfRatesTo, selectedCurrencyTo)
        lineDataSet.fillAlpha = 110
        lineDataSet.color = Color.CYAN
        lineDataSet.valueTextSize = 8f
        lineDataSet.valueTextColor = Color.CYAN
        lineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        lineDataSet.setDrawFilled(true)
        lineDataSet.fillColor = ContextCompat.getColor(requireContext(), R.color.teal_200)

        val dataSets = arrayListOf<ILineDataSet>()
        dataSets.add(lineDataSet)

        val xAxis = binding.lineChart.xAxis
        xAxis.valueFormatter = XAxisValueFormatter(dates.toList())
        xAxis.granularity = 1f
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.labelCount = 5
        xAxis.labelRotationAngle = -45f
        xAxis.mAxisMinimum = 0f
        binding.lineChart.setDescription(null)
        val lineData = LineData(dataSets)
        lineData.setDrawValues(true)
        binding.lineChart.data = lineData
        binding.lineChart.invalidate()


    }

    class XAxisValueFormatter(private val values: List<String>) : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            val intValue = value.toInt()

            return if (values.size > intValue && intValue >= 0) values.get(intValue) else ""

        }
    }
}
