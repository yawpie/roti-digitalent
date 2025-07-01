package com.yawpie.rotidigitalent.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.yawpie.rotidigitalent.databinding.ActivityLoginBinding
import com.yawpie.rotidigitalent.ui.bread.BreadListActivity
import com.yawpie.rotidigitalent.ui.register.RegisterActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val loginViewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.tvGotoRegister.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener {
            val username = binding.edtUsername.text.toString().trim()
            val password = binding.edtPassword.text.toString().trim()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                loginViewModel.login(username, password)
            } else {
                Toast.makeText(
                    this,
                    "Nama pengguna dan kata sandi tidak boleh kosong",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        observeLoginResult()
        loginViewModel.getUser()
        loginViewModel.user.observe(this) {
            if (it != null) {
                val intent = Intent(this, BreadListActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        loginViewModel.isLoading.observe(this) {
            binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        }
    }

    private fun observeLoginResult() {
        loginViewModel.loginResult.observe(this) { result ->
            result.onSuccess {
                Toast.makeText(this, "Login berhasil!", Toast.LENGTH_SHORT).show()
                val intent =
                    Intent(this, BreadListActivity::class.java) // Ganti dengan Activity utama Anda
                startActivity(intent)
                finish()
            }.onFailure {
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}