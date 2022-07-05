package com.giniapps.currentweather.screens

import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.giniapps.currentweather.R

open class BaseFragment : Fragment() {
    private lateinit var launcher: ActivityResultLauncher<Array<String>>

    private var useLocation: () -> Unit = {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initPermissionLauncher()
    }

    fun askLocationPermission(
        useLocation: () -> Unit
    ) {
        this.useLocation = useLocation
        when {
            permissionsGranted() -> this.useLocation()

            shouldShowRational() -> locationPermissionRationale()

            else -> launcher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    private fun initPermissionLauncher() {
        launcher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    false
                ) -> {
                    useLocation()
                }
                permissions.getOrDefault(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    false
                ) -> {
                    useLocation()
                }
                else -> {
                    locationPermissionRationale()
                }
            }
        }
    }

    private fun shouldShowRational() =
        shouldShowRequestPermissionRationale(
            Manifest.permission.ACCESS_FINE_LOCATION
        ) || shouldShowRequestPermissionRationale(
            Manifest.permission.ACCESS_COARSE_LOCATION
        )

    private fun locationPermissionRationale() {
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

    private fun permissionsGranted() =
        ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    requireContext(),
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) == PackageManager.PERMISSION_GRANTED
}