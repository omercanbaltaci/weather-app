package com.example.finalproject.ui

import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.finalproject.R
import com.example.finalproject.adapters.AutocompleteListAdapter
import com.example.finalproject.adapters.ViewPagerAdapter
import com.example.finalproject.base.BaseFragment
import com.example.finalproject.base.BaseViewItemClickListener
import com.example.finalproject.databinding.FragmentMainBinding
import com.example.finalproject.ui.weatherapp.model.Autocomplete
import com.example.finalproject.ui.weatherapp.model.ResultCurrent
import com.example.finalproject.ui.weatherapp.viewmodel.MainViewModel
import com.example.finalproject.util.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MainFragment : BaseFragment<MainViewModel, FragmentMainBinding>(), KoinComponent {
    private var weatherList = mutableListOf<ResultCurrent>()
    //private lateinit var cld: ConnectionLiveData

    private val cld: ConnectionLiveData by inject()
    override val mViewModel: MainViewModel by viewModel()

    override fun getLayoutID() = R.layout.fragment_main

    override fun observeLiveData() {
        mViewModel.prepareCurrentsFromDB()

        mViewModel.onAutocompleteFetched.observe(this, {
            dataBinding.mainViewState = it
            dataBinding.executePendingBindings()
            dataBinding.autoComplete.setAdapter(
                AutocompleteListAdapter(
                    requireContext(),
                    it.getAutocompleteList(),
                    object : BaseViewItemClickListener<Autocomplete> {
                        override fun onItemClicked(clickedObject: Autocomplete, id: Int) {
                            when (id) {
                                R.id.autocompleteContainer -> {
                                    dataBinding.autoComplete.setText(clickedObject.name)
                                }
                                R.id.add -> {
                                    mViewModel.prepareResult(
                                        clickedObject.name.substring(
                                            0,
                                            clickedObject.name.indexOf(",")
                                        ),
                                        clickedObject.region,
                                        clickedObject.name
                                    )
                                }
                                R.id.info -> {
                                    goToDetailsWithBundle(clickedObject.name)
                                }
                            }
                        }
                    })
            )
        })

        mViewModel.onCurrentWeatherFetched.observe(this, {
            dataBinding.currentWeatherViewState = it
            dataBinding.executePendingBindings()
            dataBinding.notificationText.gone()
            dataBinding.viewPager.visible()
            weatherList.add(ResultCurrent(it.getCurrentLocation(), it.getCurrentWeather(), true))
            dataBinding.viewPager.adapter?.notifyItemInserted(weatherList.size - 1)
        })

        mViewModel.onSingleResultFetched.observe(this, {
            if (it) showToast("This location is already bookmarked.")
            else showToast("Location has been bookmarked.")
        })

        mViewModel.onResultDelete.observe(this, {
            if (it > 0) showToast("Location has been deleted.")
        })

        if (shouldCheckInternetConnection()) {
            //cld = ConnectionLiveData(requireActivity().application)
            cld.observe(requireActivity(), { isConnected ->
                when (isConnected) {
                    true -> {
                        mViewModel.refreshLocations()

                        mViewModel.refreshCurrent.observe(this, {
                            when (it) {
                                0 -> showToast(getString(R.string.refreshing_locations))
                                1 -> {
                                    val size = weatherList.size
                                    weatherList.clear()
                                    dataBinding.viewPager.adapter?.notifyItemRangeRemoved(0, size)
                                    mViewModel.prepareCurrentsFromDB()
                                    showToast(getString(R.string.locatins_refreshed))
                                }
                                2 -> showToast(getString(R.string.no_internet))
                            }
                        })
                    }
                    else -> showToast(getString(R.string.connection_lost))
                }
            })
        }

        // Error observation
        mViewModel.onAutocompleteError.observe(this, {
            showToast(getString(R.string.error_occurred))
        })

        mViewModel.onCurrentWeatherError.observe(this, {
            showToast(getString(R.string.error_occurred))
        })

        mViewModel.onForecastError.observe(this, {
            showToast(getString(R.string.error_occurred))
        })
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun prepareView() {
        dataBinding.viewPager.adapter = ViewPagerAdapter(
            weatherList,
            object : BaseViewItemClickListener<ResultCurrent> {
                override fun onItemClicked(clickedObject: ResultCurrent, id: Int) {
                    when (id) {
                        R.id.pagerBookmark -> {
                            mViewModel.deleteResult(
                                clickedObject.location.name,
                                clickedObject.location.region
                            )
                            val clickedIndex = weatherList.indexOf(clickedObject)
                            weatherList.removeAt(clickedIndex)
                            if (weatherList.size == 0) {
                                dataBinding.notificationText.visible()
                                dataBinding.viewPager.gone()
                            }
                            dataBinding.viewPager.adapter?.notifyItemRemoved(clickedIndex)
                        }
                        R.id.pagerDetails -> {
                            goToDetailsWithBundle(clickedObject.location.name)
                        }
                    }
                }
            })

        if (weatherList.size > 0) {
            dataBinding.notificationText.gone()
            dataBinding.viewPager.visible()
        }

        dataBinding.autoComplete.textChanges()
            .debounce(300)
            .onEach { mViewModel.prepareAutocomplete(it.toString()) }
            .launchIn(lifecycleScope)
    }

    fun goToDetailsWithBundle(locationName: String) {
        val bundle = bundleOf("locationName" to locationName)
        findNavController().navigate(R.id.action_mainFragment_to_detailFragment, bundle)
    }
}