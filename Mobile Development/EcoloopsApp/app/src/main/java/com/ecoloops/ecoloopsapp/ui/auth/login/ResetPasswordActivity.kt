package com.ecoloops.ecoloopsapp.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ecoloops.ecoloopsapp.data.model.ForgotPassRequest
import com.ecoloops.ecoloopsapp.data.model.ResetPassRequest
import com.ecoloops.ecoloopsapp.data.remote.response.ResetPassResponse
import com.ecoloops.ecoloopsapp.data.remote.retrofit.ApiConfig
import com.ecoloops.ecoloopsapp.databinding.ActivityResetPasswordBinding
import com.ecoloops.ecoloopsapp.databinding.ActivitySendOtpBinding
import com.ecoloops.ecoloopsapp.utils.showAlert
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ResetPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityResetPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivBack.setOnClickListener{
            finish()
        }

        binding.emailNewEV.setText(intent.getStringExtra("email"))
        
        binding.resetPasswordButton.setOnClickListener {
            binding.resetPasswordButton.isEnabled = false
            binding.resetPasswordButton.text = "Loading..."

            val email = binding.emailNewEV.text.toString()
            val password = binding.passwordNewEV.text.toString()
            val otp = binding.otpEV.text.toString()

            if (email.isEmpty() || password.isEmpty() || otp.isEmpty()){
                showAlert(this, "Harap isi semua field!")
                binding.resetPasswordButton.isEnabled = true
                binding.resetPasswordButton.text = "Reset Password"
                return@setOnClickListener
            }

            val apiClient = ApiConfig()

            val apiService = apiClient.createApiService()

            val resetPassRequest = ResetPassRequest(email, password, otp)

            val call = apiService.resetPass(resetPassRequest)

            call.enqueue(object : Callback<ResetPassResponse> {
                override fun onResponse(
                    call: Call<ResetPassResponse>,
                    response: Response<ResetPassResponse>
                ) {
                    if (response.isSuccessful) {
                        val resetPassResponse = response.body()
                        Toast.makeText(this@ResetPasswordActivity, resetPassResponse?.message, Toast.LENGTH_LONG).show()


                        val intent = Intent(this@ResetPasswordActivity, LoginActivity::class.java)
                        startActivity(intent)


                    } else {
                        val errorBody = response.errorBody()?.string()
                        val jsonObject = JSONObject(errorBody.toString())
                        val message = jsonObject.getString("message")
                        showAlert(this@ResetPasswordActivity, message)
                    }
                    binding.resetPasswordButton.isEnabled = true
                    binding.resetPasswordButton.text = "Reset Password"

                }

                override fun onFailure(call: Call<ResetPassResponse>, t: Throwable) {
                    showAlert(this@ResetPasswordActivity, "This is a simple alertis.")
                    Log.e("RegisterActivity", "onFailure: ${t.message}")
                    binding.resetPasswordButton.isEnabled = true
                    binding.resetPasswordButton.text = "Reset Password"
                }

            })
        }
    }
}