package com.dicoding.temantani.ui.upload

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.dicoding.temantani.R
import com.dicoding.temantani.databinding.ActivityUploadProductBinding
import com.dicoding.temantani.helper.ViewModelFactory
import com.dicoding.temantani.helper.reduceFileImage
import com.dicoding.temantani.helper.uriToFile
import com.dicoding.temantani.models.ProdukViewModel
import com.dicoding.temantani.models.UploadViewModel
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class UploadProductActivity : AppCompatActivity() {

    private lateinit var uploadViewModel: UploadViewModel

    private var _activityUploadBinding : ActivityUploadProductBinding ?= null
    private val binding get() = _activityUploadBinding

    private var getFile: File? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityUploadBinding = ActivityUploadProductBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        // Pembuatan ViewModel
        uploadViewModel = obtainViewModel(this@UploadProductActivity)

        uploadViewModel.isLoading.observe(this){
            showLoading(it)
        }

        uploadViewModel.responseMessage.observe(this){
            it.getContentIfNotHandled()?.let { message ->
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }

        binding?.apply {
            btnUpload.setOnClickListener { uploadProduk() }
            imgUpload.setOnClickListener{ startGallery() }
        }
    }

    private fun uploadProduk(){
        val edNama = binding?.edNamaBarang?.text
        val edHarga = binding?.edNamaBarang?.text
        val edDeskripsi = binding?.edNamaBarang?.text

        if (getFile != null && edNama?.isNotEmpty()!! && edHarga?.isNotEmpty()!! && edDeskripsi?.isNotEmpty()!!) {
            val file = reduceFileImage(getFile as File)

            val authToken = "eyJhbGciOiJIUzI1NiJ9.Mw.72MlEAZJWe2NIdb6jRgrrxl9qdQ83xR3Up1SA3MJq14"
            val userId = "2".toRequestBody("text/plain".toMediaType())

            val requestImageFile = file.asRequestBody("image/jpeg".toMediaType())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "gambarbarang",
                file.name,
                requestImageFile
            )

            val namaBarang = binding?.edNamaBarang?.text.toString().toRequestBody("text/plain".toMediaType())
            val harga = binding?.edHargaBarang?.text.toString().toRequestBody("text/plain".toMediaType())
            val kategori = when {
                binding?.radioButtonTanaman?.isChecked == true -> "1"
                binding?.radioButtonAlatTani?.isChecked == true -> "2"
                else -> ""
            }.toRequestBody("text/plain".toMediaType())
            val deskrpisi = binding?.edtInput?.text.toString().toRequestBody("text/plain".toMediaType())

            uploadViewModel.uploadProduct(authToken,userId,imageMultipart,namaBarang,harga,kategori,deskrpisi)

        } else {
            Toast.makeText(this@UploadProductActivity, "Silakan masukkan berkas gambar terlebih dahulu.", Toast.LENGTH_SHORT).show()
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg = result.data?.data as Uri
            selectedImg.let { uri ->
                val myFile = uriToFile(uri, this@UploadProductActivity)
                getFile = myFile
                binding?.imgUpload?.setImageURI(uri)
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

    private fun obtainViewModel(activity: AppCompatActivity) : UploadViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(UploadViewModel::class.java)
    }

    private fun setupView() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }

    private fun showLoading(state: Boolean) { binding?.progressBar?.visibility = if (state) View.VISIBLE else View.GONE }
}