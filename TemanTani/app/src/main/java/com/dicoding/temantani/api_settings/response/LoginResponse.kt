package com.dicoding.temantani.api_settings.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(

	@field:SerializedName("data")
	val data: Data? = null,

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)

data class Data(

	@field:SerializedName("nama")
	val nama: String? = null,

	@field:SerializedName("userid")
	val userid: Int? = null,

	@field:SerializedName("token")
	val token: String? = null
)
