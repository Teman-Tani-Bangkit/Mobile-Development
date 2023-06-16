package com.dicoding.temantani.models

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.temantani.api_settings.ApiConfig
import com.dicoding.temantani.api_settings.response.DeteksiResponse
import com.dicoding.temantani.api_settings.response.UploadResponse
import com.dicoding.temantani.helper.Event
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeteksiViewModel(application: Application) : ViewModel() {
    private val _uploadDeteksiResponse = MutableLiveData<DeteksiResponse>()
    val uploadDeteksiResponse: LiveData<DeteksiResponse> = _uploadDeteksiResponse

    private val _responseMessage = MutableLiveData<Event<String>>()
    val responseMessage: LiveData<Event<String>> = _responseMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun detectImage(authToken: String, image: MultipartBody.Part, kategori : RequestBody) {

        _isLoading.value = true

        val client = ApiConfig.getApiService().uploadGambarDeteksi(authToken, image, kategori)

        client.enqueue(object : Callback<DeteksiResponse> {
            override fun onResponse(
                call: Call<DeteksiResponse>,
                response: Response<DeteksiResponse>
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false

                    if (response.body() != null) {
                        _uploadDeteksiResponse.value = response.body()
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

            override fun onFailure(call: Call<DeteksiResponse>, t: Throwable) {
                _isLoading.value = false
                _responseMessage.value = Event(t.message.toString())
                Log.e(TAG, "onFailure : ${t.message}")
            }

        })
    }

    companion object {
        private const val TAG = "DeteksiViewModel"
    }
}