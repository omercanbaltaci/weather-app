package com.example.finalproject.ui

import com.example.finalproject.R
import com.example.finalproject.adapters.DetailListAdapter
import com.example.finalproject.base.BaseFragment
import com.example.finalproject.databinding.FragmentDetailBinding
import com.example.finalproject.ui.weatherapp.viewmodel.MainViewModel
import com.example.finalproject.util.ConnectionLiveData
import com.example.finalproject.util.gone
import com.example.finalproject.util.visible
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class DetailFragment : BaseFragment<MainViewModel, FragmentDetailBinding>(), KoinComponent {
    //private lateinit var cld: ConnectionLiveData

    private val cld: ConnectionLiveData by inject()
    override val mViewModel: MainViewModel by viewModel()

    override fun getLayoutID(): Int = R.layout.fragment_detail

    override fun observeLiveData() {
        mViewModel.onForecastFetched.observe(this, {
            dataBinding.viewStateModel = it
            dataBinding.executePendingBindings()
            dataBinding.detailRV.adapter = DetailListAdapter(it.getHourlyList())
        })
    }

    override fun prepareView() {
        //cld = ConnectionLiveData(requireActivity().application)
        cld.observe(requireActivity(), { isConnected ->
            when (isConnected) {
                true -> {
                    dataBinding.detailNotification.gone()
                    dataBinding.detailRV.visible()
                    mViewModel.prepareForecast(arguments?.get("locationName").toString())
                }
            }
        })
    }
}