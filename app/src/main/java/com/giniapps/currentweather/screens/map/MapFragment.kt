package com.giniapps.currentweather.screens.map

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.giniapps.currentweather.R
import com.giniapps.currentweather.data.repository.models.WeatherDetailsModel
import com.giniapps.currentweather.databinding.FragmentMapBinding
import com.giniapps.currentweather.screens.BaseFragment
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MapFragment : BaseFragment(), OnMapReadyCallback {
    private lateinit var binding: FragmentMapBinding
    private val viewModel: MapScreenViewModel by inject()
    private val mapMarkers = mutableListOf<Marker>()
    private val adapter = LocationListAdapter {
        val marker = mapMarkers.first { marker -> marker.title == it.countryName }
        marker.remove()

        viewModel.removeLocation(it)
    }
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMapBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.locationsList.adapter = adapter
        setBottomSheetBehaviour()
        askLocationPermission(::locationPermissionsGranted)
    }

    private fun setBottomSheetBehaviour() {
        bottomSheetBehavior = BottomSheetBehavior.from(binding.bottomSheet)
        bottomSheetBehavior.peekHeight = 60.toPx()
        bottomSheetBehavior.addBottomSheetCallback(
            object : BottomSheetBehavior.BottomSheetCallback() {
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    binding.expandIcon.setImageDrawable(
                        ContextCompat.getDrawable(requireContext(),
                            when (newState) {
                                BottomSheetBehavior.STATE_EXPANDED ->
                                    R.drawable.ic_arrow_down
                                else ->
                                    R.drawable.ic_arrow_up
                            }
                        )
                    )
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {}
            }
        )
    }

    private fun locationPermissionsGranted() {
        viewModel.initViewModel()
        setupMap()
    }

    private fun setupMap() {
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
    }

    override fun onMapReady(map: GoogleMap) {
        map.setOnMapClickListener {
            viewModel.addLocation(it)
        }
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.collectLatest {
                        it.details.forEach { det ->
                            if (det != WeatherDetailsModel.emptyObject()) {
                                map.addMarker(
                                    MarkerOptions().position(
                                        LatLng(
                                            det.location.latitude,
                                            det.location.longitude
                                        )
                                    ).title(det.countryName)
                                ).also { marker ->
                                    if (marker != null)
                                        mapMarkers.add(marker)
                                }
                            }
                        }
                        with(binding) {
                            progressContainer.isVisible =
                                it.state == MapScreenUIState.State.LOADING
                            title.text = getString(R.string.bottom_sheet_title, it.locations.size)
                            adapter.items = it.locations
                            titleContainer.setOnClickListener {
                                when (bottomSheetBehavior.state) {
                                    BottomSheetBehavior.STATE_EXPANDED ->
                                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
                                    BottomSheetBehavior.STATE_COLLAPSED ->
                                        bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun Int.toPx() =
        this * requireContext().resources.displayMetrics.density.toInt()
}