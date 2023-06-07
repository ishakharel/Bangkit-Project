package com.ecoloops.ecoloopsapp.ui.scan

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
import com.ecoloops.ecoloopsapp.data.preference.LoginPreference
import com.ecoloops.ecoloopsapp.data.preference.UploadWastePreference
import com.ecoloops.ecoloopsapp.data.remote.response.UploadWasteResponse
import com.ecoloops.ecoloopsapp.data.remote.retrofit.ApiConfig
import com.ecoloops.ecoloopsapp.databinding.ActivityUploadWasteBinding
import com.ecoloops.ecoloopsapp.ui.camera.CameraActivity
import com.ecoloops.ecoloopsapp.ui.home.HomeActivity
import com.ecoloops.ecoloopsapp.utils.reduceFileImage
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class UploadWasteActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadWasteBinding
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
        binding = ActivityUploadWasteBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val isGallery = intent.getBooleanExtra("isGallery", false)
        val image = intent.getStringExtra("picture")

        if (isGallery) {
            Log.e("TAG", "onCreate: $image")
            getFile = File(image)
            Toast.makeText(this, "isGallery: $image", Toast.LENGTH_SHORT).show()
            binding.uploadWasteLayout.imagePickerView.setImageURI(image?.toUri())
        }

        binding.uploadWasteLayout.uploadWasteButton.setOnClickListener {
            uploadWaste()
        }

        binding.uploadWasteLayout.cameraButton.setOnClickListener {
            startCameraX()
        }
    }

    private fun startCameraX() {
        val intent = Intent(this, CameraActivity::class.java)
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
                binding.uploadWasteLayout.imagePickerView.setImageBitmap(BitmapFactory.decodeFile(file.path))
                binding.uploadWasteLayout.cameraButton.text = "RETAKE PHOTO"
            }

        }
    }

    private fun uploadWaste() {
        if (getFile != null) {
            val file = reduceFileImage(getFile as File)

            binding.uploadWasteLayout.uploadWasteButton.isEnabled = false
            binding.uploadWasteLayout.uploadWasteButton.text = "Mengunggah..."


            val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "file",
                file.name,
                requestImageFile
            )

            val apiClient = ApiConfig()
            val apiService = apiClient.createApiService()

            val userPreferences = LoginPreference(this)
            val uploadWastePreferences = UploadWastePreference(this)
            val userData = userPreferences.getUser()

            val uploadImageRequest = apiService.uploadWaste("Bearer ${userData.token}",imageMultipart)
            uploadImageRequest.enqueue(object : Callback<UploadWasteResponse> {
                override fun onResponse(
                    call: Call<UploadWasteResponse>,
                    response: Response<UploadWasteResponse>
                ) {
                    if (response.isSuccessful) {
                        val responseBody = response.body()
                        val id = responseBody?.data?.id.toString()
                        val name = responseBody?.data?.name.toString()
                        val category = responseBody?.data?.category.toString()
                        val descriptionRecycle = responseBody?.data?.description_recycle.toString()
                        val date = responseBody?.data?.date.toString()
                        val points = responseBody?.data?.points.toString().toInt()
                        val image = responseBody?.data?.image.toString()

                        uploadWastePreferences.setUploadWaste(id, name, category, descriptionRecycle, date, points, image)
//                        Toast.makeText(this@UploadWasteActivity, responseBody?.message, Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@UploadWasteActivity, ResultWasteActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(this@UploadWasteActivity, "gagal di sini", Toast.LENGTH_SHORT).show()
                    }
                    binding.uploadWasteLayout.uploadWasteButton.isEnabled = true
                    binding.uploadWasteLayout.uploadWasteButton.text = "Upload Waste"
                }
                override fun onFailure(call: Call<UploadWasteResponse>, t: Throwable) {
                    binding.uploadWasteLayout.uploadWasteButton.isEnabled = true
                    binding.uploadWasteLayout.uploadWasteButton.text = "Upload Waste"
                    Toast.makeText(this@UploadWasteActivity, t.message, Toast.LENGTH_SHORT).show()
                }
            })

        } else {
            Toast.makeText(this@UploadWasteActivity, "Silakan masukkan berkas gambar terlebih dahulu.", Toast.LENGTH_SHORT).show()
        }
        val onBackPressedCallback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                startActivity(Intent(this@UploadWasteActivity, HomeActivity::class.java))
                finish()
            }
        }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }
}