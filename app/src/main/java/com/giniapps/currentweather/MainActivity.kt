package com.giniapps.currentweather

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.giniapps.currentweather.screens.main.MainScreenViewModel
import com.giniapps.currentweather.screens.main.UIState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {
    private val viewModel: MainScreenViewModel by inject()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initData()
    }

    private fun initData() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    viewModel.uiState.collectLatest { state ->
//                        when(state.state) {
//                            UIState.State.LOADING -> TODO()
//                            UIState.State.SUCCESS -> TODO()
//                            UIState.State.ERROR -> TODO()
//                        }
                        Log.d(TAG, "$state state collected from weatherDetailsFlow")
                    }
                }
            }
        }
    }

    companion object {
        private const val TAG = "ActivityDebug"
    }
}