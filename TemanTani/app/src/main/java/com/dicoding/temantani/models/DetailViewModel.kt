package com.dicoding.temantani.models

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.temantani.api_settings.ApiConfig
import com.dicoding.temantani.api_settings.response.DetailResponse
import com.dicoding.temantani.ui.detail.DetailActivity
import retrofit2.Call
import retrofit2.Response

class DetailViewModel(application: Application) : ViewModel() {
    private val _produkDetailResponse = MutableLiveData<DetailResponse>()
    val produkDetailResponse : LiveData<DetailResponse> = _produkDetailResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    init {
        getProdukDetail(DetailActivity.ID.toString(), DetailActivity.TOKEN.toString())
    }

    private fun getProdukDetail(idBarang : String, authToken : String){
        _isLoading.value = true

        val client = ApiConfig.getApiService().getDetailProduk(idBarang, authToken)

        client.enqueue(object : retrofit2.Callback<DetailResponse>{
            override fun onResponse(
                call: Call<DetailResponse>,
                response: Response<DetailResponse>
            ) {
                if(response.isSuccessful){
                    _produkDetailResponse.value = response.body()
                    _isLoading.value = false
                }
            }

            override fun onFailure(call: Call<DetailResponse>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
                _isLoading.value = false
            }

        })
    }

    companion object{
        private const val TAG = "DetailViewModel"
    }
}