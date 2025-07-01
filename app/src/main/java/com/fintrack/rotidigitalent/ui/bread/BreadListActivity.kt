package com.fintrack.rotidigitalent.ui.bread

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.fintrack.rotidigitalent.data.Bread
import com.fintrack.rotidigitalent.databinding.ActivityBreadListBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BreadListActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBreadListBinding
    private lateinit var viewModel: BreadViewModel
    private lateinit var adapter: BreadListAdapter
    private val permissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            if (permissions[Manifest.permission.CAMERA] == false ||
                permissions[Manifest.permission.READ_EXTERNAL_STORAGE] == false || permissions[Manifest.permission.ACCESS_FINE_LOCATION] == false ||
                permissions[Manifest.permission.ACCESS_COARSE_LOCATION] == false
            ) {
                Toast.makeText(this, "Permissions are required to proceed.", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBreadListBinding.inflate(layoutInflater)
        setContentView(binding.root)


        adapter = BreadListAdapter(emptyList())
        viewModel = ViewModelProvider(this)[BreadViewModel::class.java]
        binding.rvBreadList.adapter = adapter
    }

    fun getBreads(): List<Bread>{

        return emptyList()

    }
}