package com.example.quizonline.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.quizonline.databinding.ActivitySignUpBinding

class SignUpActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySignUpBinding
    private lateinit var viewModel: SignUpViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[SignUpViewModel::class.java]

        observeViewModel()

        binding.signupButton.setOnClickListener {
            val name = binding.editTextName.text.toString()
            val email = binding.editTextEmailAddress.text.toString()
            val password = binding.editTextPassword.text.toString()
            viewModel.signUpUser(name, email, password)
        }
    }

    private fun observeViewModel() {
        viewModel.isLoading.observe(this) { isLoading ->

            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.signUpSuccess.observe(this) { isSuccess ->
            if (isSuccess) {
                Toast.makeText(this, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }

        viewModel.errorMessage.observe(this) { message ->
            if (message.isNotEmpty()) {
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }
    }
}
