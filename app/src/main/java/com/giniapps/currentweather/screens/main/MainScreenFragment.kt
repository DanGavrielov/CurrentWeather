package com.giniapps.currentweather.screens.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.giniapps.currentweather.R
import com.giniapps.currentweather.databinding.FragmentMainScreenBinding
import com.giniapps.currentweather.screens.BaseFragment
import com.squareup.picasso.Picasso
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainScreenFragment : BaseFragment() {
    private val viewModel: MainScreenViewModel by inject()
    private lateinit var binding: FragmentMainScreenBinding
    private val adapter = TrackedLocationsAdapter { }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMainScreenBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        askLocationPermission(::locationPermissionsGranted)
    }

    private fun locationPermissionsGranted() {
        viewModel.initViewModel()
        binding.locationsList.adapter = adapter
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.collectLatest {
                        with(binding) {
                            progressContainer.isVisible =
                                it.state == MainScreenUIState.State.LOADING
                            countryName.text = it.currentLocationDetails.countryName
                            temp.text = getString(
                                R.string.temperature_template,
                                it.currentLocationDetails.temperature
                            )
                            summary.text = it.currentLocationDetails.summary
                            if (it.currentLocationDetails.iconUrl.isNotEmpty()) {
                                Picasso.get()
                                    .load(it.currentLocationDetails.iconUrl)
                                    .into(weatherIcon)
                            }
                            adapter.items = it.details
                            editLocationsButton.setOnClickListener {
                                findNavController().navigate(R.id.action_mainScreenFragment_to_mapFragment)
                            }
                        }
                    }
                }
            }
        }
    }

    companion object {
        private const val TAG = "MainScreenFragDebug"
    }
}