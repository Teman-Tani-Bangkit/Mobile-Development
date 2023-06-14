package com.dicoding.temantani.models

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.temantani.api_settings.ApiConfig
import com.dicoding.temantani.api_settings.response.ProdukResponse
import retrofit2.Call
import retrofit2.Response

class ProdukViewModel(application: Application) : ViewModel() {

    private val _produkResponse = MutableLiveData<ProdukResponse>()
    val produkResponse : LiveData<ProdukResponse> = _produkResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    init {
        fetchProduk()
    }

    fun fetchProdukByKategori(kategori : String){
        _isLoading.value = true

        val client = ApiConfig.getApiService().getProdukByKategori(kategori, "eyJhbGciOiJIUzI1NiJ9.Mw.72MlEAZJWe2NIdb6jRgrrxl9qdQ83xR3Up1SA3MJq14")

        client.enqueue(object : retrofit2.Callback<ProdukResponse>{
            override fun onResponse(
                call: Call<ProdukResponse>,
                response: Response<ProdukResponse>
            ) {

                if(response.isSuccessful){
                    _isLoading.value = false
                    _produkResponse.value = response.body()

                } else {
                    Log.e(TAG, "Gagal Fetching")
                }

            }

            override fun onFailure(call: Call<ProdukResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure : ${t.message}")
            }

        })
    }

    fun fetchProduk(){
        _isLoading.value = true

        val client = ApiConfig.getApiService().getProduk("eyJhbGciOiJIUzI1NiJ9.Mw.72MlEAZJWe2NIdb6jRgrrxl9qdQ83xR3Up1SA3MJq14")

        client.enqueue(object : retrofit2.Callback<ProdukResponse>{
            override fun onResponse(
                call: Call<ProdukResponse>,
                response: Response<ProdukResponse>
            ) {

                if(response.isSuccessful){
                    _isLoading.value = false
                    _produkResponse.value = response.body()

                } else {
                    Log.e(TAG, "Gagal Fetching")
                }

            }

            override fun onFailure(call: Call<ProdukResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure : ${t.message}")
            }

        })
    }

    companion object {
        private const val  TAG = "ProdukViewModel"
    }
}