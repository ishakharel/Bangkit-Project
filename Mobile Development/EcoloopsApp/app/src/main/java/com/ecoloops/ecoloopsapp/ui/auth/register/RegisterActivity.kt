package com.ecoloops.ecoloopsapp.ui.auth.register

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.style.ForegroundColorSpan
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.ecoloops.ecoloopsapp.R
import com.ecoloops.ecoloopsapp.databinding.ActivityRegisterBinding
import com.ecoloops.ecoloopsapp.ui.auth.login.LoginActivity

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

        val spannable = SpannableStringBuilder(getText(R.string.lets_register))
        spannable.setSpan(
            ForegroundColorSpan(Color.BLACK),
            0, // start
            26, // end
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