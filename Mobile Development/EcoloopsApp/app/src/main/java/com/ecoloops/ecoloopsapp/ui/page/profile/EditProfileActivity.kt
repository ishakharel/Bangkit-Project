package com.ecoloops.ecoloopsapp.ui.page.profile

import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.ecoloops.ecoloopsapp.R
import com.ecoloops.ecoloopsapp.data.model.EditProfileRequest
import com.ecoloops.ecoloopsapp.data.preference.LoginPreference
import com.ecoloops.ecoloopsapp.data.remote.response.DetailCategoryResponse
import com.ecoloops.ecoloopsapp.data.remote.response.ResetPassResponse
import com.ecoloops.ecoloopsapp.data.remote.response.UploadPhotoResponse
import com.ecoloops.ecoloopsapp.data.remote.response.UserDetailResponse
import com.ecoloops.ecoloopsapp.data.remote.retrofit.ApiConfig
import com.ecoloops.ecoloopsapp.databinding.ActivityEditProfileBinding
import com.ecoloops.ecoloopsapp.databinding.ActivityFormEditProfileBinding
import com.ecoloops.ecoloopsapp.ui.camera.CameraActivity2
import com.ecoloops.ecoloopsapp.ui.custom_view.CustomAlertDialog
import com.ecoloops.ecoloopsapp.ui.page.home.DetailCategoryActivity
import com.ecoloops.ecoloopsapp.ui.page.home.HomeActivity
import com.ecoloops.ecoloopsapp.utils.apiDateFormat
import com.ecoloops.ecoloopsapp.utils.reduceFileImage
import com.ecoloops.ecoloopsapp.utils.showAlert
import com.ecoloops.ecoloopsapp.utils.withDateFormat
import com.google.android.material.datepicker.MaterialDatePicker
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.util.Calendar

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFormEditProfileBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val status = getIntent().getStringExtra("status")
        if(status == "success"){
            CustomAlertDialog(this@EditProfileActivity, "Sukses Mengubah Profile", R.drawable.custom_success).show()
        }

        dataProfile()

        binding.editTextDateOfBirth.setOnClickListener {
            val c = Calendar.getInstance()

            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { view, year, monthOfYear, dayOfMonth ->
                    val dateNew = (year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth.toString() + "T00:00:00.000Z")
                    binding.editTextDateOfBirth.setText(dateNew.withDateFormat())
                },
                year,
                month,
                day
            )
            datePickerDialog.show()
        }

        binding.buttonSave.setOnClickListener() {
            editProfile()
        }

        binding.cameraButton.setOnClickListener {
            startActivity(Intent(this@EditProfileActivity, EditPhotoActivity::class.java))
        }
    }

    private fun dataProfile(){
        val apiClient = ApiConfig()
        val apiService = apiClient.createApiService()

        val userPreferences = LoginPreference(this)
        val userData = userPreferences.getUser()

        val call = apiService.getUserDetail("Bearer ${userData.token}")

        call.enqueue(object : Callback<UserDetailResponse> {
            override fun onResponse(
                call: Call<UserDetailResponse>,
                response: Response<UserDetailResponse>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    val name = responseBody?.data?.name.toString()
                    val email = responseBody?.data?.email.toString()
                    val address = responseBody?.data?.address.toString()
                    val gender = responseBody?.data?.gender.toString()
                    val age = responseBody?.data?.age.toString()
                    val job = responseBody?.data?.job.toString()
                    val dob = responseBody?.data?.dob.toString()
                    val image = responseBody?.data?.image.toString()

                    userPreferences.setEmailUsername(email, name)
                    userPreferences.setImgUrl(image)

                    binding.editTextEmail.setText(email)
                    binding.editTextName.setText(name)
                    if(gender == "Male") {
                        binding.radioButtonMale.setChecked(true)
                        binding.radioButtonFemale.setChecked(false)
                    }else {
                        binding.radioButtonFemale.setChecked(true)
                        binding.radioButtonMale.setChecked(false)
                    }
                    binding.editTextDateOfBirth.setText(dob.withDateFormat())
                    binding.editTextAge.setText(age)
                    binding.editTextAddress.setText(address)
                    binding.editTextProficiency.setText(job)
                    if(image != ""){
                        Glide.with(this@EditProfileActivity)
                            .load(image)
                            .fitCenter()
                            .into(binding.circleIvAvatar)
                    }

                } else {
                    val errorBody = response.errorBody()?.string()
                    val jsonObject = JSONObject(errorBody.toString())
                    val message = jsonObject.getString("message")
                    showAlert(this@EditProfileActivity, message)
                }

            }

            override fun onFailure(call: Call<UserDetailResponse>, t: Throwable) {
                showAlert(this@EditProfileActivity, "This is a simple alertis.")
                Log.e("Detail Category", "onFailure: ${t.message}")
            }

        })
    }

    private fun editProfile(){
        val email = binding.editTextEmail.text.toString()
        val name = binding.editTextName.text.toString()
        var gender = ""
        if(binding.radioButtonMale.isChecked()){
            gender = "Male"
        }else{
            gender = "Female"
        }
        val dob = binding.editTextDateOfBirth.text.toString().apiDateFormat()
//        CustomAlertDialog(this@EditProfileActivity, dob, R.drawable.custom_error).show()

        val age = binding.editTextAge.text.toString()
        val address = binding.editTextAddress.text.toString()
        val job = binding.editTextProficiency.text.toString()

        if (email.isEmpty() || name.isEmpty() || gender.isEmpty() || dob.isEmpty() || age.toString().isEmpty() || address.isEmpty() || job.isEmpty()) {
            CustomAlertDialog(this@EditProfileActivity, "Harap Isi Semua Field!", R.drawable.custom_error).show()
            binding.buttonSave.isEnabled = true
            binding.buttonSave.text = "Loading"
        }

        val apiClient = ApiConfig()
        val apiService = apiClient.createApiService()

        val userPreferences = LoginPreference(this)
        val userData = userPreferences.getUser()

        val editProfileRequest = EditProfileRequest(email, name, address, gender,  age.toInt(), job, dob)
        val call = apiService.editProfile("Bearer ${userData.token}", editProfileRequest)
        call.enqueue(object : Callback<ResetPassResponse> {
            override fun onResponse(
                call: Call<ResetPassResponse>,
                response: Response<ResetPassResponse>
            ) {
                if (response.isSuccessful) {
                    val intent = Intent(this@EditProfileActivity, EditProfileActivity::class.java)
                    intent.putExtra("status", "success");
                    startActivity(intent)
                    finish()
                } else {
                    val errorBody = response.errorBody()?.string()
                    val jsonObject = JSONObject(errorBody.toString())
                    val message = jsonObject.getString("message")
                    CustomAlertDialog(this@EditProfileActivity, message, R.drawable.custom_error).show()
                }
                binding.buttonSave.isEnabled = true
                binding.buttonSave.text = "Edit Profile"
            }
            override fun onFailure(call: Call<ResetPassResponse>, t: Throwable) {
                binding.buttonSave.isEnabled = true
                binding.buttonSave.text = "Edit Profile"
                Toast.makeText(this@EditProfileActivity, t.message, Toast.LENGTH_SHORT).show()
            }
        })
    }
}