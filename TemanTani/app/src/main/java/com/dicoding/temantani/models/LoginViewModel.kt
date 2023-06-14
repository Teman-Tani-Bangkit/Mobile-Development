package com.dicoding.temantani.models

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.temantani.api_settings.ApiConfig
import com.dicoding.temantani.api_settings.response.LoginResponse
import com.dicoding.temantani.helper.Event
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response

class LoginViewModel(application: Application) : ViewModel() {

    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse : LiveData<LoginResponse> = _loginResponse

    private val _responseMessage = MutableLiveData<Event<String>>()
    val responseMessage : LiveData<Event<String>> = _responseMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    fun postLoginUser(email : String, password : String){
        _isLoading.value = true

        val clientResponse = ApiConfig.getApiService().userLogin(email, password)

        clientResponse.enqueue(object : retrofit2.Callback<LoginResponse>{
            override fun onResponse(
                call: Call<LoginResponse>,
                response: Response<LoginResponse>
            ) {

                if(response.isSuccessful){
                    _isLoading.value = false

                    _loginResponse.value = response.body()
                    _responseMessage.value = Event(_loginResponse.value?.message.toString())

                } else {
                    _isLoading.value = false
                    try {
                        val errorBody = response.errorBody()?.string()
                        val jsonObject = JSONObject(errorBody)
                        val message = jsonObject.getString("message")
                        _responseMessage.value = Event(message)

                    } catch (e: Exception) {
                        Log.e(TAG, "Exception: ${e.message}")
                    }
                }

            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                _isLoading.value = false
                _responseMessage.value = Event(t.message.toString())
                Log.e(TAG, "onFailure : ${t.message}")
            }

        })
    }

    companion object {
        private const val TAG = "LoginViewModel"
    }
}




























