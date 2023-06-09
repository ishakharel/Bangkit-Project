package com.ecoloops.ecoloopsapp.ui.auth.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ecoloops.ecoloopsapp.R
import com.ecoloops.ecoloopsapp.data.model.ForgotPassRequest
import com.ecoloops.ecoloopsapp.data.model.LoginRequest
import com.ecoloops.ecoloopsapp.data.remote.response.ForgotPassResponse
import com.ecoloops.ecoloopsapp.data.remote.retrofit.ApiConfig
import com.ecoloops.ecoloopsapp.databinding.ActivityLoginBinding
import com.ecoloops.ecoloopsapp.databinding.ActivitySendOtpBinding
import com.ecoloops.ecoloopsapp.ui.custom_view.CustomAlertDialog
import com.ecoloops.ecoloopsapp.utils.showAlert
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SendOtpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySendOtpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySendOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivBack.setOnClickListener{
            finish()
        }

        binding.sendOtpButton.setOnClickListener{
            binding.sendOtpButton.isEnabled = false
            binding.sendOtpButton.text = "Loading..."

            val email = binding.emailOtpEV.text.toString()

            if (email.isEmpty()){
                CustomAlertDialog(this@SendOtpActivity, "Harap Masukan Emailmu!", R.drawable.custom_error).show()
                binding.sendOtpButton.isEnabled = true
                binding.sendOtpButton.text = "Send OTP"
                return@setOnClickListener
            }

            val apiClient = ApiConfig()

            val apiService = apiClient.createApiService()

            val forgotPassRequest = ForgotPassRequest(email)

            val call = apiService.forgotPass(forgotPassRequest)

            call.enqueue(object : Callback<ForgotPassResponse> {
                override fun onResponse(
                    call: Call<ForgotPassResponse>,
                    response: Response<ForgotPassResponse>
                ) {
                    if (response.isSuccessful) {
                        val forgotPassResponse = response.body()
                        Toast.makeText(this@SendOtpActivity, forgotPassResponse?.message, Toast.LENGTH_LONG).show()


                        val intent = Intent(this@SendOtpActivity, ResetPasswordActivity::class.java)
                        intent.putExtra("email", email)
                        startActivity(intent)


                    } else {
                        val errorBody = response.errorBody()?.string()
                        val jsonObject = JSONObject(errorBody.toString())
                        val message = jsonObject.getString("message")
                        CustomAlertDialog(this@SendOtpActivity, message, R.drawable.custom_error).show()
                    }
                    binding.sendOtpButton.isEnabled = true
                    binding.sendOtpButton.text = "Send OTP"

                }

                override fun onFailure(call: Call<ForgotPassResponse>, t: Throwable) {
                    CustomAlertDialog(this@SendOtpActivity, "${t.message}", R.drawable.custom_error).show()
                    binding.sendOtpButton.isEnabled = true
                    binding.sendOtpButton.text = "Send OTP"
                }

            })
        }
    }
}