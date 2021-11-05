package com.example.finalproject.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.finalproject.R
import com.example.finalproject.base.BaseViewItemClickListener
import com.example.finalproject.databinding.ViewpagerItemBinding
import com.example.finalproject.ui.weatherapp.model.ResultCurrent
import com.example.finalproject.util.gone
import com.example.finalproject.util.visible

class ViewPagerAdapter(val list: List<ResultCurrent>) :
    RecyclerView.Adapter<ItemViewHolder>() {
    private var itemClickListener: BaseViewItemClickListener<ResultCurrent>? = null

    constructor(
        list: List<ResultCurrent>,
        itemClickListener: BaseViewItemClickListener<ResultCurrent>
    ) : this(list) {
        this.itemClickListener = itemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context),
                R.layout.viewpager_item,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val resultCurrent = this.list[position]
        holder.populate(resultCurrent)
        holder.setOnItemClickListener(resultCurrent, this.itemClickListener)
        if (resultCurrent.isCelsiusSelected) {
            holder.celsiusText.visible()
            holder.fahrenheitText.gone()
            holder.feelsLikeCelsiusText.visible()
            holder.feelsLikeFahrenheitText.gone()
            holder.toggleGroup.check(R.id.celcius)
        } else {
            holder.celsiusText.gone()
            holder.fahrenheitText.visible()
            holder.feelsLikeCelsiusText.gone()
            holder.feelsLikeFahrenheitText.visible()
            holder.toggleGroup.check(R.id.fahrenheit)
        }
    }

    override fun getItemCount() = this.list.size
}

class ItemViewHolder(private val binding: ViewpagerItemBinding) :
    RecyclerView.ViewHolder(binding.root) {
    val celsiusText = binding.temperatureC
    val fahrenheitText = binding.temperatureF
    val feelsLikeCelsiusText = binding.feelsLikeTempC
    val feelsLikeFahrenheitText = binding.feelsLikeTempF
    val toggleGroup = binding.toggleGroup

    fun populate(resultCurrent: ResultCurrent) {
        binding.resultCurrent = resultCurrent
        binding.executePendingBindings()

        binding.celcius.setOnClickListener {
            binding.temperatureC.visible()
            binding.feelsLikeTempC.visible()
            binding.temperatureF.gone()
            binding.feelsLikeTempF.gone()
            resultCurrent.isCelsiusSelected = true
        }
        binding.fahrenheit.setOnClickListener {
            binding.temperatureF.visible()
            binding.feelsLikeTempF.visible()
            binding.temperatureC.gone()
            binding.feelsLikeTempC.gone()
            resultCurrent.isCelsiusSelected = false
        }
    }

    fun setOnItemClickListener(
        resultCurrent: ResultCurrent,
        itemClickListener: BaseViewItemClickListener<ResultCurrent>?
    ) {
        binding.pagerBookmark.setOnClickListener {
            itemClickListener!!.onItemClicked(resultCurrent, it.id)
        }
        binding.pagerDetails.setOnClickListener {
            itemClickListener!!.onItemClicked(resultCurrent, it.id)
        }
    }
}