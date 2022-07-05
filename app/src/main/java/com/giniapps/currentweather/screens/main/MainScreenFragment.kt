package com.giniapps.currentweather.screens.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.giniapps.currentweather.databinding.FragmentMainScreenBinding
import com.giniapps.currentweather.screens.LocationFragment
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainScreenFragment : LocationFragment() {
    private val viewModel: MainScreenViewModel by inject()
    private lateinit var binding: FragmentMainScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initPermissionLauncher(
            onGranted = ::locationPermissionsGranted,
            onRejected = {
                locationPermissionRationale()
            }
        )
    }
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
        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.collectLatest { state ->
                        when (state.state) {
                            MainScreenUIState.State.LOADING -> {}
                            MainScreenUIState.State.SUCCESS -> {}
                        }
                        Log.d(TAG, "$state state collected from weatherDetailsFlow")
                    }
                }
            }
        }
    }

    companion object {
        private const val TAG = "MainScreenFragDebug"
    }
}