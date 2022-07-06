package com.giniapps.currentweather.screens.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.giniapps.currentweather.R
import com.giniapps.currentweather.databinding.FragmentMapBinding
import com.giniapps.currentweather.screens.BaseFragment
import com.giniapps.currentweather.screens.main.MainScreenViewModel
import com.giniapps.currentweather.screens.main.MainScreenUIState
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MapFragment : BaseFragment(), OnMapReadyCallback {
    private lateinit var binding: FragmentMapBinding
    private val viewModel: MapScreenViewModel by inject()
    private val adapter = LocationListAdapter { }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        askLocationPermission(::locationPermissionsGranted)
    }

    private fun locationPermissionsGranted() {
        viewModel.initViewModel()
        binding.locationsList.adapter = adapter
        setupMap()
    }

    private fun setupMap() {
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.collectLatest {
                        it.details.forEach { det ->
                            map.addMarker(
                                MarkerOptions().position(
                                    LatLng(
                                        det.location.latitude,
                                        det.location.longitude
                                    )
                                ).title(det.countryName)
                            )
                        }
                        with(binding) {
                            progressContainer.isVisible =
                                it.state == MapScreenUIState.State.LOADING
                            title.text = getString(R.string.bottom_sheet_title, it.locations.size)
                            adapter.items = it.locations
                        }
                    }
                }
            }
        }
    }
}