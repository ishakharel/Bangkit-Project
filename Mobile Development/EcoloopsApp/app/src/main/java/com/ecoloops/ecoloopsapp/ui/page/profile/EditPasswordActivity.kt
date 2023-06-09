package com.ecoloops.ecoloopsapp.ui.page.profile

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.ecoloops.ecoloopsapp.R
import com.ecoloops.ecoloopsapp.data.model.EditPassRequest
import com.ecoloops.ecoloopsapp.data.preference.LoginPreference
import com.ecoloops.ecoloopsapp.data.remote.response.ResetPassResponse
import com.ecoloops.ecoloopsapp.data.remote.retrofit.ApiConfig
import com.ecoloops.ecoloopsapp.databinding.ActivityEditPasswordBinding
import com.ecoloops.ecoloopsapp.ui.custom_view.CustomAlertDialog
import com.ecoloops.ecoloopsapp.utils.showAlert
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EditPasswordActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditPasswordBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivBack.setOnClickListener{
            finish()
        }

        binding.resetPasswordButton.setOnClickListener {

            val passwordOld = binding.passwordOldEV.text.toString()
            val passwordNew = binding.passwordNewEV.text.toString()

            if (passwordOld.isEmpty() || passwordNew.isEmpty()) {
                CustomAlertDialog(this@EditPasswordActivity, "Harap Isi Semua Field!", R.drawable.custom_error).show()
                binding.resetPasswordButton.isEnabled = true
                binding.resetPasswordButton.text = "Edit Password"
                return@setOnClickListener
            }

            val apiClient = ApiConfig()
            val apiService = apiClient.createApiService()

            val userPreferences = LoginPreference(this)
            val userData = userPreferences.getUser()

            val editPassRequest = EditPassRequest(passwordOld, passwordNew)
            val call = apiService.editPassword("Bearer ${userData.token}", editPassRequest)
            call.enqueue(object : Callback<ResetPassResponse> {
                override fun onResponse(
                    call: Call<ResetPassResponse>,
                    response: Response<ResetPassResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        startActivity(Intent(this@EditPasswordActivity, ProfileActivity::class.java))
                        finish()
                        Toast.makeText(this@EditPasswordActivity, responseBody?.message, Toast.LENGTH_SHORT).show()

                    } else {
                        val errorBody = response.errorBody()?.string()
                        val jsonObject = JSONObject(errorBody.toString())
                        val message = jsonObject.getString("message")
                        CustomAlertDialog(this@EditPasswordActivity, message, R.drawable.custom_error).show()
                    }
                    binding.resetPasswordButton.isEnabled = true
                    binding.resetPasswordButton.text = "Edit Password"
                }
                override fun onFailure(call: Call<ResetPassResponse>, t: Throwable) {
                    binding.resetPasswordButton.isEnabled = true
                    binding.resetPasswordButton.text = "Edit Password"
                    Toast.makeText(this@EditPasswordActivity, t.message, Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}