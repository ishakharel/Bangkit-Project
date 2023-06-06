package com.ecoloops.ecoloopsapp.ui.auth.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.ecoloops.ecoloopsapp.R
import com.ecoloops.ecoloopsapp.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val spannable = SpannableStringBuilder(getText(R.string.welcome_back))
        spannable.setSpan(
            ForegroundColorSpan(Color.BLACK),
            0, // start
            24, // end
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

        val loginButton = ObjectAnimator.ofFloat(binding.loginLayout.loginButton, View.ALPHA, 1f).setDuration(300)
        val registerButton = ObjectAnimator.ofFloat(binding.loginLayout.registerButton, View.ALPHA, 1f).setDuration(300)

        val together = AnimatorSet().apply {
            playTogether(loginButton, registerButton)
        }

        AnimatorSet().apply {
            playSequentially(emailTextView, emailEditTextLayout, emailEditText, passwordTextView, passwordEditTextLayout, passwordEditText, together)
            start()
        }
    }
}