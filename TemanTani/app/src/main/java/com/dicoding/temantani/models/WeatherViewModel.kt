package com.dicoding.temantani.models

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.temantani.api_settings.ApiWeather
import com.dicoding.temantani.api_settings.response.WeatherResponse
import com.dicoding.temantani.helper.Event
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response

class WeatherViewModel(application: Application) : ViewModel() {
    private val _weatherResponse = MutableLiveData<WeatherResponse>()
    val weatherResponse: LiveData<WeatherResponse> = _weatherResponse

    private val _responseMessage = MutableLiveData<Event<String>>()
    val responseMessage: LiveData<Event<String>> = _responseMessage

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getWeatherData(latitude : String, longitude : String){
        val client = ApiWeather.getApiService().getWeather(latitude, longitude, UNITS, ID)

        client.enqueue(object : retrofit2.Callback<WeatherResponse>{
            override fun onResponse(
                call: Call<WeatherResponse>,
                response: Response<WeatherResponse>
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false

                    if (response.body() != null) {
                        _weatherResponse.value = response.body()
                    }
                } else {
                    _isLoading.value = false
                    try {
                        val errorBody = response.errorBody()?.string()
                        val jsonObject = JSONObject(errorBody)
                        val message = jsonObject.getString("message")
                        _responseMessage.value = Event(message)
                    } catch (e: Exception) {
                        Log.e(RekomendasiViewModel.TAG, "Exception: ${e.message}")
                    }
                }
            }

            override fun onFailure(call: Call<WeatherResponse>, t: Throwable) {
                _isLoading.value = false
                _responseMessage.value = Event(t.message.toString())
                Log.e(RekomendasiViewModel.TAG, "onFailure : ${t.message}")
            }

        })
    }

    companion object {
        const val TAG = "WeatherViewModel"
        const val UNITS = "metric"
        const val ID = "a4dc11cd6f032292f9457da6161053d6"
    }
}