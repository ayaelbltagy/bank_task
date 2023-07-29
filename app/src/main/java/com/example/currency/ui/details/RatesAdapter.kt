package com.example.currency.ui.details

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.example.currency.R
import com.example.currency.databinding.RateItemBinding

class RatesAdapter(private val ratesList: Map<String, *>) :
    RecyclerView.Adapter<RatesAdapter.ViewHolder>() {


    inner class ViewHolder(@NonNull val itemBinding: RateItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RateItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var listKey = ratesList.keys.toList()
        var listValues = ratesList.values.toList()
        holder.itemBinding.key.text = listKey.get(position)
        holder.itemBinding.value.text = listValues.get(position).toString()

    }


    override fun getItemCount(): Int = ratesList.size


}