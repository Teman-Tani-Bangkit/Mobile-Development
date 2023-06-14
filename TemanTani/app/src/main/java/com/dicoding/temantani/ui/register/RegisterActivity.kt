package com.dicoding.temantani.ui.register

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.dicoding.temantani.R
import com.dicoding.temantani.databinding.ActivityRegisterBinding
import com.dicoding.temantani.helper.ViewModelFactory
import com.dicoding.temantani.models.LoginViewModel
import com.dicoding.temantani.models.RegisterViewModel

class RegisterActivity : AppCompatActivity() {
    private lateinit var registerViewModel : RegisterViewModel

    private var _activityRegisterBinding : ActivityRegisterBinding ?= null

    private val binding get() = _activityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityRegisterBinding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        // Pembuatan View Model Register
        registerViewModel = obtainViewModel(this@RegisterActivity)

        registerViewModel.isLoading.observe(this){ showLoading(it) }

        registerViewModel.responseMessage.observe(this){
            it.getContentIfNotHandled()?.let { message ->
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }

        setupView()
        binding?.apply {
            btnRegister.setOnClickListener { userRegister() }
        }
    }

    private fun userRegister(){
        val edNamaText = binding?.edNama?.text.toString()
        val edEmailText = binding?.edEmail?.text.toString()
        val edPhoneText = binding?.edTelephone?.text.toString()
        val edPasswordText = binding?.edPassword?.text.toString()

        if(edNamaText.isEmpty() || edEmailText.isEmpty() || edPasswordText.isEmpty() || edPhoneText.isEmpty()){
            warnEmptyEd(edNamaText, edEmailText, edPhoneText ,edPasswordText)

        } else if(edNamaText.isNotEmpty() && edEmailText.isNotEmpty() && edPasswordText.isNotEmpty() && edPhoneText.isNotEmpty()){
            registerViewModel.postRegisterUser(edEmailText, edPasswordText, edPhoneText, edNamaText)
        }
    }

    private fun warnEmptyEd(name : String, email : String, phone : String, password : String){
        if(name.isEmpty()){
            binding?.edNama?.setError("Mohon masukkan nama anda terlebih dahulu")
        }

        if(email.isEmpty()){
            binding?.edEmail?.setError("Mohon masukkan email anda terlebih dahulu")
        }

        if(phone.isEmpty()){
            binding?.edTelephone?.setError("Mohon masukkan No.Telepon terlebih dahulu")
        }

        if(password.isEmpty()){
            binding?.edPassword?.setError("Mohon masukkan password terlebih dahulu")
        }
    }

    private fun obtainViewModel(activity: AppCompatActivity) : RegisterViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(RegisterViewModel::class.java)
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