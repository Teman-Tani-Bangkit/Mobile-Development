package com.dicoding.temantani.ui.login

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
import com.dicoding.temantani.databinding.ActivityLoginBinding
import com.dicoding.temantani.helper.ViewModelFactory
import com.dicoding.temantani.models.LoginViewModel
import com.dicoding.temantani.ui.register.RegisterActivity

class LoginActivity : AppCompatActivity(){

    private lateinit var loginViewModel : LoginViewModel

    private var _activityLoginBinding : ActivityLoginBinding ?= null
    private val binding get() = _activityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityLoginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        // Proses Pembuatan Login View Model
        loginViewModel = obtainViewModel(this@LoginActivity)

        loginViewModel.isLoading.observe(this){ showLoading(it) }

        loginViewModel.responseMessage.observe(this){
            it.getContentIfNotHandled()?.let { message ->
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
            }
        }

        setupView()

        binding?.apply {
            btnLogin.setOnClickListener { userLogin() }
            tvToRegister.setOnClickListener { moveToRegister() }
        }
    }

    private fun warnEmptyEd(email : String, password : String){
        if(email.isEmpty()){
            binding?.edLoginEmail?.setError("Please Input Your Email")
        }

        if(password.isEmpty()){
            binding?.edLoginPassword?.setError("Please Input Your Password")
        }
    }

    private fun userLogin(){
        val edEmailText = binding?.edLoginEmail?.text.toString()
        val edPasswordText = binding?.edLoginPassword?.text.toString()

        if(edEmailText.isEmpty() || edPasswordText.isEmpty()){
            warnEmptyEd(edEmailText, edPasswordText)

        } else if(edEmailText.isNotEmpty() && edPasswordText.isNotEmpty()){
            loginViewModel.postLoginUser(edEmailText, edPasswordText)
        }
    }

    private fun moveToRegister(){
        val intentToRegister = Intent(this, RegisterActivity::class.java)
        startActivity(intentToRegister)
    }

    private fun obtainViewModel(activity: AppCompatActivity) : LoginViewModel {
        val factory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, factory).get(LoginViewModel::class.java)
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