package com.ecoloops.ecoloopsapp.ui.page.profile

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.ecoloops.ecoloopsapp.data.preference.LoginPreference
import com.ecoloops.ecoloopsapp.data.remote.response.UploadPhotoResponse
import com.ecoloops.ecoloopsapp.data.remote.retrofit.ApiConfig
import com.ecoloops.ecoloopsapp.databinding.ActivityEditProfileBinding
import com.ecoloops.ecoloopsapp.ui.camera.CameraActivity2
import com.ecoloops.ecoloopsapp.utils.reduceFileImage
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class EditProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityEditProfileBinding
    private var getFile: File? = null

    companion object {
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
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
        binding = ActivityEditProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userProfilePreference = LoginPreference(this)
        val userProfile = userProfilePreference.getUser()

        if(userProfile.image != ""){
            Glide.with(this@EditProfileActivity)
                .load(userProfile.image)
                .fitCenter()
                .into(binding.circleIvAvatar)
        }

        val isGallery = intent.getBooleanExtra("isGallery", false)
        val image = intent.getStringExtra("picture")

        if (isGallery) {
            Log.e("TAG", "onCreate: $image")
            getFile = File(image)
            Toast.makeText(this, "isGallery: $image", Toast.LENGTH_SHORT).show()
            binding.circleIvAvatar.setImageURI(image?.toUri())
        }

        binding.submitEditButton.setOnClickListener {
            uploadStory()
        }

        binding.cameraButton.setOnClickListener {
            startCameraX()
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

            binding.submitEditButton.isEnabled = false
            binding.submitEditButton.text = "Mengunggah..."

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
                        Toast.makeText(this@EditProfileActivity, "gagal", Toast.LENGTH_SHORT).show()
                    }
                    binding.submitEditButton.isEnabled = true
                    binding.submitEditButton.text = "Upload Story"
                }
                override fun onFailure(call: Call<UploadPhotoResponse>, t: Throwable) {
                    binding.submitEditButton.isEnabled = true
                    binding.submitEditButton.text = "Upload Story"
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
}