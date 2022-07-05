package com.giniapps.currentweather.screens

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.res.stringResource
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.giniapps.currentweather.R
import kotlinx.coroutines.launch
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

open class LocationFragment : Fragment() {
    private lateinit var launcher: ActivityResultLauncher<Array<String>>

    fun initPermissionLauncher(
        onGranted: () -> Unit,
        onRejected: () -> Unit
    ) {
        launcher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    false
                ) -> {
                    onGranted()
                }
                permissions.getOrDefault(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    false
                ) -> {
                    onGranted()
                }
                else -> {
                    onRejected()
                }
            }
        }
    }

    fun askLocationPermission(
        useLocation: () -> Unit
    ) {
        when {
            permissionsGranted() -> useLocation()

            shouldShowRational() -> locationPermissionRationale()

            else -> launcher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun shouldShowRational() =
        shouldShowRequestPermissionRationale(
            Manifest.permission.ACCESS_FINE_LOCATION
        ) || shouldShowRequestPermissionRationale(
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

    fun locationPermissionRationale() {
        val dialog = AlertDialog.Builder(requireContext())
            .setTitle(getString(R.string.rationale_title))
            .setMessage(getString(R.string.rationale_message))
            .setPositiveButton(R.string.rationale_positive_button) { _, _ ->
                launcher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
            .setNegativeButton(R.string.rationale_negative_button) { _, _ ->
                requireActivity().finish()
            }.create()
        dialog.show()
    }

    fun permissionsGranted() =
        ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
}