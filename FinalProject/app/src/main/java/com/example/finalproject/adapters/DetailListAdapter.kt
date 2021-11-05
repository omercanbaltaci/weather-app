package com.example.finalproject.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.R
import com.example.finalproject.databinding.RowDetailBinding
import com.example.finalproject.ui.weatherapp.model.Hour
import com.example.finalproject.util.gone
import com.example.finalproject.util.visible
import com.google.android.material.button.MaterialButtonToggleGroup

class DetailListAdapter(var list: List<Hour>) : RecyclerView.Adapter<DetailViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DetailViewHolder {
        return DetailViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.row_detail,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: DetailViewHolder, position: Int) {
        val hour = this.list[position]
        holder.populate(hour)
        if (!hour.isCelsiusSelected) {
            holder.toggleGroup.check(R.id.celcius)
            holder.temperatureC.visible()
            holder.temperatureF.gone()
            holder.feelsLikeC.visible()
            holder.feelsLikeF.gone()
        } else {
            holder.toggleGroup.check(R.id.fahrenheit)
            holder.temperatureC.gone()
            holder.temperatureF.visible()
            holder.feelsLikeC.gone()
            holder.feelsLikeF.visible()
        }
    }

    override fun getItemCount() = this.list.size
}

class DetailViewHolder(private val binding: RowDetailBinding) :
    RecyclerView.ViewHolder(binding.root) {
    var toggleGroup: MaterialButtonToggleGroup = binding.toggleGroup
    var temperatureC: TextView = binding.temperatureC
    var temperatureF: TextView = binding.temperatureF
    var feelsLikeC: TextView = binding.detailsFeesLikeC
    var feelsLikeF: TextView = binding.detailsFeesLikeF

    fun populate(hour: Hour) {
        binding.hour = hour
        binding.executePendingBindings()

        binding.celcius.setOnClickListener {
            binding.temperatureC.visible()
            binding.temperatureF.gone()
            binding.detailsFeesLikeC.visible()
            binding.detailsFeesLikeF.gone()
            hour.isCelsiusSelected = false
        }
        binding.fahrenheit.setOnClickListener {
            binding.temperatureC.gone()
            binding.temperatureF.visible()
            binding.detailsFeesLikeC.gone()
            binding.detailsFeesLikeF.visible()
            hour.isCelsiusSelected = true
        }
        binding.root.setOnClickListener {
            Log.e("dfsf", hour.isCelsiusSelected.toString())
        }
    }
}