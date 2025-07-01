package com.fintrack.rotidigitalent.ui.bread

import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.fintrack.rotidigitalent.data.repository.BreadRepository
import com.fintrack.rotidigitalent.databinding.ActivityAddBreadBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddBreadActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddBreadBinding
    private var currentImageUri: Uri? = null
    @Inject
    lateinit var breadRepository: BreadRepository

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


        binding.ivBreadPhoto.setOnClickListener {
            imagePickerLauncher.launch("image/*")
        }

        binding.btnSaveBread.setOnClickListener {
            saveBread()
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

        // 3. Implementasi logika penyimpanan menggunakan BreadRepository
        // Memanggil fungsi addBread dari repository
        val result = breadRepository.addBread(name, description, imageUriString)

        if (result > -1) {
            Toast.makeText(this, "Bread '$name' saved successfully", Toast.LENGTH_LONG).show()
            finish() // Menutup activity setelah menyimpan
        } else {
            Toast.makeText(this, "Failed to save bread", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}