package com.dicoding.temantani.models

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.temantani.api_settings.ApiConfig
import com.dicoding.temantani.api_settings.response.UploadResponse
import com.dicoding.temantani.helper.Event
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UploadViewModel(application: Application) : ViewModel() {
    private val _uploadResponse = MutableLiveData<UploadResponse>()
    val uploadResponse: LiveData<UploadResponse> = _uploadResponse

    private val _responseMessage = MutableLiveData<Event<String>>()
    val responseMessage: LiveData<Event<String>> = _responseMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun uploadProduct(authToken: String, userId: RequestBody, image: MultipartBody.Part, namabarang: RequestBody, harga: RequestBody, kategori: RequestBody, deskripsi: RequestBody) {

        _isLoading.value = true

        val client = ApiConfig.getApiService().uploadProduk(authToken, userId, image, namabarang, harga, kategori, deskripsi)

        client.enqueue(object : Callback<UploadResponse> {
            override fun onResponse(call: Call<UploadResponse>, response: Response<UploadResponse>) {
                if (response.isSuccessful) {
                    _isLoading.value = false

                    if (response.body() != null) {
                        _uploadResponse.value = response.body()
                        _responseMessage.value = Event(_uploadResponse.value?.message.toString())
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

            override fun onFailure(call: Call<UploadResponse>, t: Throwable) {
                _isLoading.value = false
                _responseMessage.value = Event(t.message.toString())
                Log.e(TAG, "onFailure : ${t.message}")
            }
        })
    }

    companion object {
        private const val TAG = "UploadProductViewModel"
    }
}