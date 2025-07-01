package com.yawpie.rotidigitalent.ui.locate

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.yawpie.rotidigitalent.databinding.ActivitySetLocationBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LocationSettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySetLocationBinding

    private val viewModel: LocationViewModel by viewModels()

    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
            ) {
                fetchCurrentLocation()
            } else {
                Toast.makeText(this, "Izin lokasi diperlukan.", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        loadLastKnownLocation()

        viewModel.latitude.observe(this) { lat ->
            binding.tvLatitudeValue.text = lat.toString()
        }
        viewModel.longitude.observe(this) { lon ->
            binding.tvLongitudeValue.text = lon.toString()
        }

        binding.btnLocate.setOnClickListener {
            checkAndRequestLocationPermission()
        }
        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }
    }


    private fun checkAndRequestLocationPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(
                        this, Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED -> {

                fetchCurrentLocation()
            }

            else -> {

                requestPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        }
    }

    private fun fetchCurrentLocation() {
        Toast.makeText(this, "Mencari lokasi...", Toast.LENGTH_SHORT).show()
        viewModel.updateLocation()
    }
//        userRepository.getCurrentLocation(this) { lat, lon ->
//            if (lat != null && lon != null) {
//
//                updateLocationUI(lat, lon)
//                lifecycleScope.launch {
//                    userRepository.setLastKnownLocation(this@LocationSettingsActivity, lat, lon) //
//                }
//            } else {
//                Toast.makeText(this, "Gagal mendapatkan lokasi.", Toast.LENGTH_SHORT).show()
//            }
//        }


    private fun loadLastKnownLocation() {
        viewModel.getLastKnownLocation()
    }

//    private fun updateLocationUI(latitude: Float, longitude: Float) {
//        binding.tvLatitudeValue.text = latitude.toString()
//        binding.tvLongitudeValue.text = longitude.toString()
//    }
}