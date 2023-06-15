package com.dicoding.temantani.api_settings

import com.dicoding.temantani.api_settings.response.LoginResponse
import com.dicoding.temantani.api_settings.response.ProdukResponse
import com.dicoding.temantani.api_settings.response.RegisterResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {

    @GET("/tampilkanProduk")
    fun getProduk(
        @Header("key") authToken: String
    ): Call<ProdukResponse>

    @GET("/tampilkanKategori/{kategori}")
    fun getProdukByKategori(
        @Path("kategori") kategori: String,
        @Header("key") authToken: String
    ): Call<ProdukResponse>

    @FormUrlEncoded
    @POST("/login")
    fun userLogin(
        @Field("email") email: String,
        @Field("password") password: String,
    ): Call<LoginResponse>

    @FormUrlEncoded
    @POST("/register")
    fun userRegister(
        @Field("email") email: String,
        @Field("password") password: String,
        @Field("notelepon") phone: String,
        @Field("nama") name: String,
    ): Call<RegisterResponse>

}