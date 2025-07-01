package com.yawpie.rotidigitalent.ui.bread

import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.yawpie.rotidigitalent.databinding.ActivityAddBreadBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddBreadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddBreadBinding
    private var currentImageUri: Uri? = null


    private val viewModel: BreadViewModel by viewModels()

    private val imagePickerLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            currentImageUri = it
            binding.ivBreadPhoto.setImageURI(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBreadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Add New Bread"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }




        binding.btnInputImage.setOnClickListener {
            imagePickerLauncher.launch("image/*")
        }

        binding.btnSaveBread.setOnClickListener {
            saveBread()
        }

        viewModel.uploadSuccess.observe(this) { event ->
            event.getContentIfNotHandled()?.let { success ->
                if (success) {
                    Toast.makeText(this, "Bread added successfully", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Failed to add bread", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun saveBread() {
        val name = binding.etBreadName.text.toString().trim()
        val description = binding.etBreadDescription.text.toString().trim()

        if (name.isEmpty()) {
            binding.etBreadName.error = "Name cannot be empty"
            return
        }

        val imageUriString = currentImageUri?.toString()
        if (imageUriString == null) {
            Toast.makeText(this, "Please select a photo", Toast.LENGTH_SHORT).show()
            return
        }

        viewModel.addBread(name, description, imageUriString)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}