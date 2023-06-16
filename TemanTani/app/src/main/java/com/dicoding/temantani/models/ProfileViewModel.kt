package com.dicoding.temantani.models

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.temantani.api_settings.ApiConfig
import com.dicoding.temantani.api_settings.response.ProfileResponse
import com.dicoding.temantani.ui.profile.ProfileActivity
import retrofit2.Call
import retrofit2.Response

class ProfileViewModel(application: Application) : ViewModel() {
    private val _profileResponse = MutableLiveData<ProfileResponse>()
    val profileResponse : LiveData<ProfileResponse> = _profileResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    init {
        getProfileData(ProfileActivity.ID.toString(), ProfileActivity.TOKEN.toString())
    }

    fun searchProduk(authToken: String, namaProduk : String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().searchProdukProfile(authToken, namaProduk)
        client.enqueue(object : retrofit2.Callback<ProfileResponse>{
            override fun onResponse(
                call: Call<ProfileResponse>,
                response: Response<ProfileResponse>
            ) {

                if(response.isSuccessful){
                    _isLoading.value = false
                    _profileResponse.value = response.body()

                } else {
                    Log.e(TAG, "Gagal Fetching")
                }

            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure : ${t.message}")
            }

        })
    }

     fun getProfileData(idUser : String, authToken : String){
        _isLoading.value = true

        val client = ApiConfig.getApiService().getDataProfile(idUser, authToken)

        client.enqueue(object : retrofit2.Callback<ProfileResponse>{
            override fun onResponse(
                call: Call<ProfileResponse>,
                response: Response<ProfileResponse>
            ) {
                if(response.isSuccessful){
                    _profileResponse.value = response.body()
                    _isLoading.value = false
                }
            }

            override fun onFailure(call: Call<ProfileResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
                _isLoading.value = false
            }

        })
    }

    companion object{
        private const val TAG = "ProfileViewModel"
    }
}