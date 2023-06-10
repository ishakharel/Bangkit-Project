package com.ecoloops.ecoloopsapp.ui.auth.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ecoloops.ecoloopsapp.MainActivity
import com.ecoloops.ecoloopsapp.R
import com.ecoloops.ecoloopsapp.data.model.RegisterRequest
import com.ecoloops.ecoloopsapp.data.remote.response.RegisterResponse
import com.ecoloops.ecoloopsapp.data.remote.retrofit.ApiConfig
import com.ecoloops.ecoloopsapp.databinding.ActivityRegisterBinding
import com.ecoloops.ecoloopsapp.ui.auth.login.LoginActivity
import com.ecoloops.ecoloopsapp.utils.showAlert
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.registerLayout.backButton.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.registerLayout.registerButton.setOnClickListener {
            binding.registerLayout.registerButton.isEnabled = false
            binding.registerLayout.registerButton.text = "Loading..."

            val email = binding.registerLayout.emailEditText.text.toString()
            val password = binding.registerLayout.passwordEditText.text.toString()
            val username = binding.registerLayout.nameEditText.text.toString()

            if (email.isEmpty() || password.isEmpty() || username.isEmpty()) {
                showAlert(this, "Please fill all the fields")
                binding.registerLayout.registerButton.isEnabled = true
                binding.registerLayout.registerButton.text = "Register"
                return@setOnClickListener
            }

            val apiClient = ApiConfig()

            val apiService = apiClient.createApiService()

            val registerRequest = RegisterRequest(
                username, email, password
            )

            val call = apiService.register(registerRequest)

            call.enqueue(object : Callback<RegisterResponse> {
                override fun onResponse(
                    call: Call<RegisterResponse>,
                    response: Response<RegisterResponse>
                ) {
                    if (response.isSuccessful) {
                        val registerResponse = response.body()
                        Toast.makeText(this@RegisterActivity, registerResponse?.message, Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                        intent.putExtra("email", email)
                        startActivity(intent)
                    } else {
                        val errorBody = response.errorBody()?.string()
                        val jsonObject = JSONObject(errorBody)
                        val message = jsonObject.getString("message")
                        showAlert(this@RegisterActivity, message)
                    }

                    binding.registerLayout.registerButton.isEnabled = true
                    binding.registerLayout.registerButton.text = "Register"
                }

                override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                    showAlert(this@RegisterActivity, "This is a simple alertis.")
                    Log.e("RegisterActivity", "onFailure: ${t.message}")
                    binding.registerLayout.registerButton.isEnabled = true
                    binding.registerLayout.registerButton.text = "Register"
                }
            })
        }

        val spannable = SpannableStringBuilder(getText(R.string.lets_register))
        spannable.setSpan(
            ForegroundColorSpan(getColor(R.color.colorPrimary)),
            26, // start
            35, // end
            Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )

        binding.registerLayout.tvWelcomeBack.text = spannable

        playAnimation()
        setupToolbar()
    }
    private fun setupToolbar() {
        title = resources.getString(R.string.register)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    private fun playAnimation() {
        val registerTextView = ObjectAnimator.ofFloat(binding.registerLayout.tvWelcomeBack, View.ALPHA, 1f).setDuration(300)

        val nameTextView = ObjectAnimator.ofFloat(binding.registerLayout.nameTextView, View.ALPHA, 1f).setDuration(300)
        val nameEditTextLayout = ObjectAnimator.ofFloat(binding.registerLayout.nameEditTextLayout, View.ALPHA, 1f).setDuration(300)
        val nameEditText = ObjectAnimator.ofFloat(binding.registerLayout.nameEditText, View.ALPHA, 1f).setDuration(300)

        val emailTextView = ObjectAnimator.ofFloat(binding.registerLayout.emailTextView, View.ALPHA, 1f).setDuration(300)
        val emailEditTextLayout = ObjectAnimator.ofFloat(binding.registerLayout.emailEditTextLayout, View.ALPHA, 1f).setDuration(300)
        val emailEditText = ObjectAnimator.ofFloat(binding.registerLayout.emailEditText, View.ALPHA, 1f).setDuration(300)

        val passwordTextView = ObjectAnimator.ofFloat(binding.registerLayout.passwordTextView, View.ALPHA, 1f).setDuration(300)
        val passwordEditTextLayout = ObjectAnimator.ofFloat(binding.registerLayout.passwordEditTextLayout, View.ALPHA, 1f).setDuration(300)
        val passwordEditText = ObjectAnimator.ofFloat(binding.registerLayout.passwordEditText, View.ALPHA, 1f).setDuration(300)

        val confirmPasswordTextView = ObjectAnimator.ofFloat(binding.registerLayout.confirmPasswordTextView, View.ALPHA, 1f).setDuration(300)
        val confirmPasswordEditTextLayout = ObjectAnimator.ofFloat(binding.registerLayout.confirmPasswordEditTextLayout, View.ALPHA, 1f).setDuration(300)
        val confirmPasswordEditText = ObjectAnimator.ofFloat(binding.registerLayout.confirmPasswordEditText, View.ALPHA, 1f).setDuration(300)

        val loginButton = ObjectAnimator.ofFloat(binding.registerLayout.registerButton, View.ALPHA, 1f).setDuration(300)
        val backButton = ObjectAnimator.ofFloat(binding.registerLayout.backButton, View.ALPHA, 1f).setDuration(300)

        val together = AnimatorSet().apply {
            playTogether(loginButton, backButton)
        }

        AnimatorSet().apply {
            playSequentially(
                registerTextView,
                nameTextView, nameEditTextLayout, nameEditText,
                emailTextView, emailEditTextLayout, emailEditText,
                passwordTextView, passwordEditTextLayout, passwordEditText,
                confirmPasswordTextView, confirmPasswordEditTextLayout, confirmPasswordEditText,
                together
            )
            start()
        }
    }
}