package com.dicoding.temantani.ui.deteksi

import android.Manifest
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.dicoding.temantani.R
import com.dicoding.temantani.databinding.ActivityDeteksiTanamanBinding
import com.dicoding.temantani.databinding.ActivityUploadProductBinding
import com.dicoding.temantani.db.UserAuth
import com.dicoding.temantani.db.UserPreference
import com.dicoding.temantani.helper.CameraActivity
import com.dicoding.temantani.helper.ViewModelFactory
import com.dicoding.temantani.helper.reduceFileImage
import com.dicoding.temantani.helper.rotateFile
import com.dicoding.temantani.helper.uriToFile
import com.dicoding.temantani.models.DeteksiViewModel
import com.dicoding.temantani.models.UploadViewModel
import com.dicoding.temantani.ui.detail.DetailActivity
import com.dicoding.temantani.ui.upload.UploadProductActivity
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class DeteksiTanamanActivity : AppCompatActivity() {
    private lateinit var userAuth : UserAuth

    private lateinit var deteksiViewModel: DeteksiViewModel

    private var _activityDeteksiTanamanBinding : ActivityDeteksiTanamanBinding?= null
    private val binding get() = _activityDeteksiTanamanBinding

    private var getFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityDeteksiTanamanBinding = ActivityDeteksiTanamanBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        userAuth()
        TOKEN = userAuth.token.toString()

        val kategori = intent.getStringExtra(CATEGORY_DETECT)
        CATEGORY_DETECT = kategori

        // Pembuatan ViewModel
        deteksiViewModel = obtainViewModel(this@DeteksiTanamanActivity)

        deteksiViewModel.uploadDeteksiResponse.observe(this){response ->
            val dialogBinding = layoutInflater.inflate(R.layout.pop_up, null)
            val popUpDeteksi = Dialog(this)
            popUpDeteksi.setContentView(dialogBinding)

            val imageTanamanSakit = dialogBinding.findViewById<ImageView>(R.id.image_tanaman_sakit)
            val namaTanamanSakit = dialogBinding.findViewById<TextView>(R.id.nama_tanaman_sakit)
            val deskripsiTanamanSakit = dialogBinding.findViewById<TextView>(R.id.deskripsi_tanaman_sakit)

            Glide.with(this)
                .load("https://storage.googleapis.com/temantani-bucket/tanaman/${response.gambar}")
                .into(imageTanamanSakit)
            namaTanamanSakit.text = response.nama
            deskripsiTanamanSakit.text = response.deskripsi

            popUpDeteksi.setCancelable(true)
            popUpDeteksi.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            popUpDeteksi.show()
        }

        deteksiViewModel.isLoading.observe(this){
            showLoading(it)
        }

        deteksiViewModel.responseMessage.observe(this){
            it.getContentIfNotHandled()?.let { message ->
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }

        cameraPermission()

        binding?.apply {
            getGallery.setOnClickListener { startGallery() }
            getCamera.setOnClickListener { openCameraX() }
            btnDeteksi.setOnClickListener { uploadImageDetect() }
            iconBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        }
    }

    private fun uploadImageDetect(){
        if (getFile != null) {
            val file = reduceFileImage(getFile as File)
            val authToken = TOKEN.toString()
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "gambar",
                file.name,
                requestImageFile
            )
            val kategori = CATEGORY_DETECT.toString().toRequestBody("text/plain".toMediaType())

            deteksiViewModel.detectImage(authToken, imageMultipart, kategori)

        } else {
            Toast.makeText(this@DeteksiTanamanActivity, "Silakan masukkan berkas gambar terlebih dahulu.", Toast.LENGTH_SHORT).show()
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                val myFile = uriToFile(uri, this@DeteksiTanamanActivity)
                getFile = myFile
                binding?.imgDeteksi?.setImageURI(uri)
            }
        }
    }

    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
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

            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean

            myFile?.let { file ->
//                rotateFile(file, isBackCamera)
                getFile = file

                binding?.imgDeteksi?.setImageBitmap(BitmapFactory.decodeFile(file.path))
            }
        }
    }

    private fun openCameraX(){
        val intent = Intent(this@DeteksiTanamanActivity, CameraActivity::class.java)
        launcherIntentCameraX.launch(intent)
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private fun cameraPermission(){
        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(
                this,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
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

    private fun obtainViewModel(activity: AppCompatActivity) : DeteksiViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(DeteksiViewModel::class.java)
    }

    private fun userAuth(){
        val userPref = UserPreference(this)
        userAuth = userPref.getUser()
    }

    private fun showLoading(state: Boolean) { binding?.progressBar?.visibility = if (state) View.VISIBLE else View.GONE }

    companion object {
        var CATEGORY_DETECT : String ?= null
        var TOKEN : String ?= null
        const val CAMERA_X_RESULT = 200
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}