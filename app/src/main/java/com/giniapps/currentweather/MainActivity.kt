package com.giniapps.currentweather

import android.Manifest
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.giniapps.currentweather.screens.main.MainScreenUIState
import com.giniapps.currentweather.screens.main.MainScreenViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

//    private fun initData() {
//        lifecycleScope.launch {
//            repeatOnLifecycle(Lifecycle.State.STARTED) {
//                launch {
//                    viewModel.uiState.collectLatest { state ->
//                        when(state.state) {
//                            MainScreenUIState.State.LOADING -> {}
//                            MainScreenUIState.State.SUCCESS -> {}
//                        }
//                        Log.d(TAG, "$state state collected from weatherDetailsFlow")
//                    }
//                }
//            }
//        }
//    }
}