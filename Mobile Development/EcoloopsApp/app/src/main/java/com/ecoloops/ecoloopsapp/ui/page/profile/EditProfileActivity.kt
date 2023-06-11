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
import com.ecoloops.ecoloopsapp.data.preference.LoginPreference
import com.ecoloops.ecoloopsapp.data.remote.response.DetailCategoryResponse
import com.ecoloops.ecoloopsapp.data.remote.response.UploadPhotoResponse
import com.ecoloops.ecoloopsapp.data.remote.response.UserDetailResponse
import com.ecoloops.ecoloopsapp.data.remote.retrofit.ApiConfig
import com.ecoloops.ecoloopsapp.databinding.ActivityEditProfileBinding
import com.ecoloops.ecoloopsapp.databinding.ActivityFormEditProfileBinding
import com.ecoloops.ecoloopsapp.ui.camera.CameraActivity2
import com.ecoloops.ecoloopsapp.ui.custom_view.CustomAlertDialog
import com.ecoloops.ecoloopsapp.ui.page.home.DetailCategoryActivity
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
    private var getFile: File? = null

    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(android.Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this,
                    "Tidak mendapatkan permission.",
                    Toast.LENGTH_SHORT
                ).show()
                finish()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivBack.setOnClickListener{
            finish()
        }

        val isGallery = intent.getBooleanExtra("isGallery", false)
        val image = intent.getStringExtra("picture")

        if (isGallery) {
            Log.e("TAG", "onCreate: $image")
            getFile = File(image)
            Toast.makeText(this, "isGallery: $image", Toast.LENGTH_SHORT).show()
            binding.circleIvAvatar.setImageURI(image?.toUri())
        }

        binding.buttonSave.setOnClickListener {
            uploadStory()
        }

        binding.cameraButton.setOnClickListener {
            startCameraX()
        }

        dataProfile()

        binding.editTextDateOfBirth.setOnClickListener {

            // on below line we are getting
            // the instance of our calendar.
            val c = Calendar.getInstance()

            // on below line we are getting
            // our day, month and year.
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            // on below line we are creating a
            // variable for date picker dialog.
            val datePickerDialog = DatePickerDialog(
                // on below line we are passing context.
                this,
                { view, year, monthOfYear, dayOfMonth ->
                    // on below line we are setting
                    // date to our edit text.
                    val dat = (year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth.toString() + "T00:00:00.000Z")
                    binding.editTextDateOfBirth.setText(dat.withDateFormat())
                },
                // on below line we are passing year, month
                // and day for the selected date in our date picker.
                year,
                month,
                day
            )
            // at last we are calling show
            // to display our date picker dialog.
            datePickerDialog.show()
        }

        binding.radioGroupGender.setOnCheckedChangeListener { group, checkedId ->
            val radio: RadioButton = findViewById(checkedId)
            var result = "Male"
            if(radio.text == "Perempuan"){
                result = "Female"
            }
            Toast.makeText(applicationContext," On checked change :"+
                    " $result",
                Toast.LENGTH_SHORT).show()
        }
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity2::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                it.data?.getSerializableExtra("picture", File::class.java)
            } else {
                @Suppress("DEPRECATION")
                it.data?.getSerializableExtra("picture")
            } as? File
            myFile?.let { file ->
                getFile = file
                binding.circleIvAvatar.setImageBitmap(BitmapFactory.decodeFile(file.path))
            }

        }
    }


    private fun uploadStory() {
        if (getFile != null) {
            val file = reduceFileImage(getFile as File)

            binding.buttonSave.isEnabled = false
            binding.buttonSave.text = "Mengunggah..."

            val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "file",
                file.name,
                requestImageFile
            )

            val apiClient = ApiConfig()
            val apiService = apiClient.createApiService()

            val userPreferences = LoginPreference(this)
            val userData = userPreferences.getUser()

            val uploadImageRequest = apiService.uploadPhoto("Bearer ${userData.token}",imageMultipart)
            uploadImageRequest.enqueue(object : Callback<UploadPhotoResponse> {
                override fun onResponse(
                    call: Call<UploadPhotoResponse>,
                    response: Response<UploadPhotoResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        val imageUrl = responseBody?.imageUrl.toString()

                        userPreferences.setImgUrl(imageUrl)
                        startActivity(Intent(this@EditProfileActivity, ProfileActivity::class.java))
                        finish()
                        Toast.makeText(this@EditProfileActivity, responseBody?.message, Toast.LENGTH_SHORT).show()

                    } else {
                        val errorBody = response.errorBody()?.string()
                        val jsonObject = JSONObject(errorBody.toString())
                        val message = jsonObject.getString("message")
                        CustomAlertDialog(this@EditProfileActivity, message, R.drawable.custom_error).show()

                    }
                    binding.buttonSave.isEnabled = true
                    binding.buttonSave.text = "Upload Story"
                }
                override fun onFailure(call: Call<UploadPhotoResponse>, t: Throwable) {
                    binding.buttonSave.isEnabled = true
                    binding.buttonSave.text = "Upload Story"
                    Toast.makeText(this@EditProfileActivity, t.message, Toast.LENGTH_SHORT).show()
                }
            })

        } else {
            Toast.makeText(this@EditProfileActivity, "Silakan masukkan berkas gambar terlebih dahulu.", Toast.LENGTH_SHORT).show()
        }

        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                startActivity(Intent(this@EditProfileActivity, ProfileActivity::class.java))
                finish()
            }
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
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
}