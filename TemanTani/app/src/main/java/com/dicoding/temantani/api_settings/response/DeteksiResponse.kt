package com.dicoding.temantani.api_settings.response

import com.google.gson.annotations.SerializedName

data class DeteksiResponse(

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("deskripsi")
	val deskripsi: String? = null,

	@field:SerializedName("gambar")
	val gambar: String? = null
)
