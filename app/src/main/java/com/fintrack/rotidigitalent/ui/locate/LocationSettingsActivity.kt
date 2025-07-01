package com.fintrack.rotidigitalent.ui.locate

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.fintrack.rotidigitalent.data.repository.UserRepository
import com.fintrack.rotidigitalent.databinding.ActivitySetLocationBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint // Anotasi Hilt untuk injeksi dependensi
class LocationSettingsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySetLocationBinding

    @Inject // Injeksi UserRepository
    lateinit var userRepository: UserRepository

    // Launcher modern untuk meminta izin
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            if (permissions[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == true
            ) {
                // Jika izin diberikan, ambil lokasi
                fetchCurrentLocation()
            } else {
                // Jika izin ditolak, beri tahu pengguna
                Toast.makeText(this, "Izin lokasi diperlukan.", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetLocationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Muat dan tampilkan lokasi terakhir yang tersimpan saat activity dibuka
        loadLastKnownLocation()

        binding.btnLocate.setOnClickListener {
            // Ketika tombol diklik, cek izin terlebih dahulu
            checkAndRequestLocationPermission()
        }
    }

    /**
     * Memeriksa apakah izin lokasi sudah diberikan. Jika ya, ambil lokasi.
     * Jika tidak, minta izin kepada pengguna.
     */
    private fun checkAndRequestLocationPermission() {
        when {
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED ||
                    ContextCompat.checkSelfPermission(
                        this, Manifest.permission.ACCESS_COARSE_LOCATION
                    ) == PackageManager.PERMISSION_GRANTED -> {
                // Izin sudah ada, langsung ambil lokasi
                fetchCurrentLocation()
            }
            else -> {
                // Izin belum ada, luncurkan permintaan izin
                requestPermissionLauncher.launch(
                    arrayOf(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    )
                )
            }
        }
    }

    /**
     * Menggunakan UserRepository untuk mengambil lokasi saat ini dan menampilkannya.
     */
    private fun fetchCurrentLocation() {
        Toast.makeText(this, "Mencari lokasi...", Toast.LENGTH_SHORT).show()
        // Menggunakan fungsi dari UserRepository
        userRepository.getCurrentLocation(this) { lat, lon ->
            if (lat != null && lon != null) {
                // Jika lokasi berhasil didapat, update UI
                updateLocationUI(lat, lon)
                // Simpan lokasi ke SessionManager menggunakan coroutine
                lifecycleScope.launch {
                    userRepository.setLastKnownLocation(this@LocationSettingsActivity, lat, lon) //
                }
            } else {
                Toast.makeText(this, "Gagal mendapatkan lokasi.", Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Memuat lokasi terakhir dari SessionManager dan menampilkannya di UI.
     */
    private fun loadLastKnownLocation() {
        lifecycleScope.launch {
            userRepository.getLastKnownLocation().onSuccess { location -> //
                location?.let {
                    updateLocationUI(it.latitude.toFloat(), it.longitude.toFloat())
                }
            }
        }
    }

    /**
     * Helper function untuk memperbarui TextView dengan nilai latitude dan longitude.
     */
    private fun updateLocationUI(latitude: Float, longitude: Float) {
        binding.tvLatitudeValue.text = latitude.toString()
        binding.tvLongitudeValue.text = longitude.toString()
    }
}