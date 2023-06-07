package com.ecoloops.ecoloopsapp.ui.auth.login

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
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import com.ecoloops.ecoloopsapp.R
import com.ecoloops.ecoloopsapp.data.model.LoginRequest
import com.ecoloops.ecoloopsapp.data.preference.LoginPreference
import com.ecoloops.ecoloopsapp.data.remote.response.LoginResponse
import com.ecoloops.ecoloopsapp.data.remote.retrofit.ApiConfig
import com.ecoloops.ecoloopsapp.databinding.ActivityLoginBinding
import com.ecoloops.ecoloopsapp.ui.auth.register.RegisterActivity
import com.ecoloops.ecoloopsapp.ui.home.HomeActivity
import com.ecoloops.ecoloopsapp.utils.showAlert
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userPreference = LoginPreference(this@LoginActivity)

        val userData = userPreference.getUser()
        if (userData.token != "") {
            val intent = Intent(this@LoginActivity, HomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.loginLayout.registerButton.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.loginLayout.loginButton.setOnClickListener{
            binding.loginLayout.loginButton.isEnabled = false
            binding.loginLayout.loginButton.text = "Loading..."

            val email = binding.loginLayout.emailEditText.text.toString()
            val password = binding.loginLayout.passwordEditText.text.toString()

            if (email.isEmpty() || password.isEmpty()) {
                showAlert(this, "Please fill all the fields")
                binding.loginLayout.loginButton.isEnabled = true
                binding.loginLayout.loginButton.text = "Login"
                return@setOnClickListener
            }

            val apiClient = ApiConfig()

            val apiService = apiClient.createApiService()

            val loginRequest = LoginRequest(email, password)

            val call = apiService.login(loginRequest)

            call.enqueue(object : Callback<LoginResponse> {
                override fun onResponse(
                    call: Call<LoginResponse>,
                    response: Response<LoginResponse>
                ) {
                    if (response.isSuccessful) {
                        val loginResponse = response.body()
                        val id = loginResponse?.data?.id.toString()
                        val name = loginResponse?.data?.name.toString()
                        val email = loginResponse?.data?.email.toString()
                        val image = loginResponse?.data?.image.toString()
                        val points = loginResponse?.data?.points.toString().toInt()
                        val token = loginResponse?.data?.token.toString()

                        userPreference.setUser(id, name, email,image, points, token)
                        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                        startActivity(intent)


                    } else {
                        val errorBody = response.errorBody()?.string()
                        val jsonObject = JSONObject(errorBody.toString())
                        val message = jsonObject.getString("message")
                        showAlert(this@LoginActivity, message)


                    }
                    binding.loginLayout.loginButton.isEnabled = true
                    binding.loginLayout.loginButton.text = "Login"

                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    showAlert(this@LoginActivity, "This is a simple alertis.")
                    Log.e("RegisterActivity", "onFailure: ${t.message}")
                    binding.loginLayout.loginButton.isEnabled = true
                    binding.loginLayout.loginButton.text = "Login"
                }

            })



        }

        val spannable = SpannableStringBuilder(getText(R.string.welcome_back))
        spannable.setSpan(
            ForegroundColorSpan(getColor(R.color.colorPrimary)),
            25, // start
            34, // end
            Spannable.SPAN_EXCLUSIVE_INCLUSIVE
        )

        binding.loginLayout.tvWelcomeBack.text = spannable

        hideActionBar()
        playAnimation()
    }

    private fun hideActionBar() {
        supportActionBar?.hide()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.loginLayout.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val emailTextView = ObjectAnimator.ofFloat(binding.loginLayout.emailTextView, View.ALPHA, 1f).setDuration(300)
        val emailEditTextLayout = ObjectAnimator.ofFloat(binding.loginLayout.emailEditTextLayout, View.ALPHA, 1f).setDuration(300)
        val emailEditText = ObjectAnimator.ofFloat(binding.loginLayout.emailEditText, View.ALPHA, 1f).setDuration(300)

        val passwordTextView = ObjectAnimator.ofFloat(binding.loginLayout.passwordTextView, View.ALPHA, 1f).setDuration(300)
        val passwordEditTextLayout = ObjectAnimator.ofFloat(binding.loginLayout.passwordEditTextLayout, View.ALPHA, 1f).setDuration(300)
        val passwordEditText = ObjectAnimator.ofFloat(binding.loginLayout.passwordEditText, View.ALPHA, 1f).setDuration(300)

        val forgetPassword = ObjectAnimator.ofFloat(binding.loginLayout.forgetPassword, View.ALPHA, 1f).setDuration(300)

        val loginButton = ObjectAnimator.ofFloat(binding.loginLayout.loginButton, View.ALPHA, 1f).setDuration(300)
        val registerButton = ObjectAnimator.ofFloat(binding.loginLayout.registerButton, View.ALPHA, 1f).setDuration(300)

        val together = AnimatorSet().apply {
            playTogether(loginButton, registerButton)
        }

        AnimatorSet().apply {
            playSequentially(emailTextView, emailEditTextLayout, emailEditText, passwordTextView, passwordEditTextLayout, passwordEditText, forgetPassword, together)
            start()
        }

        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                finishAffinity()
            }
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }
}