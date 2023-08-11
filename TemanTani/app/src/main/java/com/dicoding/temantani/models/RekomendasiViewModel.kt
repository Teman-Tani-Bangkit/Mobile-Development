package com.dicoding.temantani.models

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.temantani.api_settings.ApiConfig
import com.dicoding.temantani.api_settings.response.RekomendasiResponse
import com.dicoding.temantani.helper.Event
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response

class RekomendasiViewModel(application: Application) : ViewModel() {
    private val _uploadRekomendasiResponse = MutableLiveData<RekomendasiResponse>()
    val uploadRekomendasiResponse: LiveData<RekomendasiResponse> = _uploadRekomendasiResponse

    private val _responseMessage = MutableLiveData<Event<String>>()
    val responseMessage: LiveData<Event<String>> = _responseMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun plantReccomendation(authToken : String, n : String, p : String, k : String, temp : String, humidity : String, ph : String, rainfall : String){

        _isLoading.value = true

        val client = ApiConfig.getApiService().getRekomendasi(authToken, n, p, k, temp, humidity, ph, rainfall)

        client.enqueue(object : retrofit2.Callback<RekomendasiResponse>{
            override fun onResponse(
                call: Call<RekomendasiResponse>,
                response: Response<RekomendasiResponse>
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false

                    if (response.body() != null) {
                        _uploadRekomendasiResponse.value = response.body()
                    }
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

            override fun onFailure(call: Call<RekomendasiResponse>, t: Throwable) {
                _isLoading.value = false
                _responseMessage.value = Event(t.message.toString())
                Log.e(TAG, "onFailure : ${t.message}")
            }

        })

    }

    companion object{
        const val TAG = "RekomendasiViewModel"
    }
}