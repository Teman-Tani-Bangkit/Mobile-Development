package com.dicoding.temantani.api_settings

import com.dicoding.temantani.api_settings.response.DetailResponse
import com.dicoding.temantani.api_settings.response.LoginResponse
import com.dicoding.temantani.api_settings.response.ProdukResponse
import com.dicoding.temantani.api_settings.response.ProfileResponse
import com.dicoding.temantani.api_settings.response.RegisterResponse
import com.dicoding.temantani.api_settings.response.UploadResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("/tampilkanProduk")
    fun searchProduk(
        @Header("key") authToken: String,
        @Query("namabarang") nama: String
    ): Call<ProdukResponse>

    @GET("/tampilkanProduk")
    fun getProduk(
        @Header("key") authToken: String
    ): Call<ProdukResponse>

    @GET("/tampilkanKategori/{kategori}")
    fun getProdukByKategori(
        @Path("kategori") kategori: String,
        @Header("key") authToken: String
    ): Call<ProdukResponse>

    @GET("/userProfil/{userid}")
    fun getDataProfile(
        @Path("userid") userid: String,
        @Header("key") authToken: String
    ): Call<ProfileResponse>

    @GET("/detailProduk/{idbarang}")
    fun getDetailProduk(
        @Path("idbarang") idbarang: String,
        @Header("key") authToken: String
    ): Call<DetailResponse>

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

    @Multipart
    @POST("/uploadProduk")
    fun uploadProduk(
        @Header("key") authToken: String,
        @Part("userid") userId: RequestBody,
        @Part gambarbarang: MultipartBody.Part,
        @Part("namabarang") namabarang: RequestBody,
        @Part("harga") harga: RequestBody,
        @Part("kategori") kategori: RequestBody,
        @Part("deskripsi") deskripsi: RequestBody
    ): Call<UploadResponse>

}