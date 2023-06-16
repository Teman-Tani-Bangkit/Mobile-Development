package com.dicoding.temantani.api_settings.response

import com.google.gson.annotations.SerializedName

data class ProfileResponse(

	@field:SerializedName("data")
	val data: DataProfile? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class DataItemProfile(

	@field:SerializedName("idbarang")
	val idbarang: Int? = null,

	@field:SerializedName("harga")
	val harga: String? = null,

	@field:SerializedName("kategori")
	val kategori: Int? = null,

	@field:SerializedName("deskripsi")
	val deskripsi: String? = null,

	@field:SerializedName("userid")
	val userid: Int? = null,

	@field:SerializedName("namabarang")
	val namabarang: String? = null,

	@field:SerializedName("gambarbarang")
	val gambarbarang: String? = null
)

data class DataProfile(

	@field:SerializedName("barang")
	val barang: List<DataItemProfile>,

	@field:SerializedName("user")
	val user: User? = null
)

data class User(

	@field:SerializedName("password")
	val password: String? = null,

	@field:SerializedName("notelepon")
	val notelepon: String? = null,

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("foto")
	val foto: String? = null,

	@field:SerializedName("userid")
	val userid: Int? = null,

	@field:SerializedName("email")
	val email: String? = null
)
