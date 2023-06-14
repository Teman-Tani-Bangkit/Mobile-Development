package com.dicoding.temantani.api_settings.response

import com.google.gson.annotations.SerializedName

data class ProdukResponse(

	@field:SerializedName("data")
	val data: List<DataItem>,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class DataItem(

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
