package com.dicoding.temantani.helper

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.temantani.models.LoginViewModel
import com.dicoding.temantani.models.ProdukViewModel
import com.dicoding.temantani.models.RegisterViewModel
import com.dicoding.temantani.models.UploadViewModel

class ViewModelFactory private constructor(private val mApplication: Application) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        if(modelClass.isAssignableFrom(LoginViewModel::class.java)){
            return LoginViewModel(mApplication) as T
        } else if(modelClass.isAssignableFrom(RegisterViewModel::class.java)){
            return RegisterViewModel(mApplication) as T
        }else if(modelClass.isAssignableFrom(ProdukViewModel::class.java)){
            return ProdukViewModel(mApplication) as T
        }else if(modelClass.isAssignableFrom(UploadViewModel::class.java)){
            return UploadViewModel(mApplication) as T
        }

        throw IllegalArgumentException("Unknown View Model Class : ${modelClass.name}")

    }

    companion object {
        @Volatile
        private var INSTANCE : ViewModelFactory ?= null

        @JvmStatic
        fun getInstance(application: Application) : ViewModelFactory {
            if(INSTANCE == null){
                synchronized(ViewModelFactory::class.java){
                    INSTANCE = ViewModelFactory(application)
                }
            }

            return INSTANCE as ViewModelFactory
        }
    }
}