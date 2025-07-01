package com.yawpie.rotidigitalent.ui.bread

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.yawpie.rotidigitalent.databinding.ActivityBreadListBinding
import com.yawpie.rotidigitalent.ui.locate.LocationSettingsActivity
import com.yawpie.rotidigitalent.ui.login.LoginActivity
import com.yawpie.rotidigitalent.ui.login.LoginViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BreadListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBreadListBinding
    private val viewModel: BreadViewModel by viewModels()
    private val loginViewModel: LoginViewModel by viewModels()
    private lateinit var adapter: BreadListAdapter
    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions[Manifest.permission.CAMERA] == false
                || permissions[Manifest.permission.READ_EXTERNAL_STORAGE] == false
                || permissions[Manifest.permission.ACCESS_FINE_LOCATION] == false
                || permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == false
            ) {
                Toast.makeText(this, "Permissions are required to proceed.", Toast.LENGTH_SHORT)
                    .show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBreadListBinding.inflate(layoutInflater)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.attributes.layoutInDisplayCutoutMode =
                WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_NEVER
        }
        setContentView(binding.root)
        binding.rvBreadList.layoutManager = LinearLayoutManager(this)
        permissionLauncher.launch(
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )

        // Observe the LiveData from the ViewModel to update the UI
        viewModel.isLoading.observe(this) { it ->
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }
        viewModel.breads.observe(this) { breadList ->
            // When the list of breads changes, update the RecyclerView adapter
            adapter = BreadListAdapter(breadList)
            binding.rvBreadList.adapter = adapter
        }


        binding.fabAddBread.setOnClickListener {
            val intent = Intent(this, AddBreadActivity::class.java)
            startActivity(intent)
        }
        binding.fabSettings.setOnClickListener {
            if (isLocationPermissionGranted()) {
                val intent = Intent(this, LocationSettingsActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Please grant location permission", Toast.LENGTH_SHORT).show()
            }
        }
        binding.fabLogout.setOnClickListener {
            loginViewModel.logout()

        }
        loginViewModel.isLoading.observe(this) {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }
        loginViewModel.user.observe(this) {
            if (it == null) {
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                Toast.makeText(this, "Logged out", Toast.LENGTH_SHORT).show()
                finish()
                return@observe
            }
        }
        viewModel.fetchBreads()
    }

    private fun isLocationPermissionGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
            this, Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
    }


    override fun onResume() {
        super.onResume()
        viewModel.fetchBreads()
    }
}