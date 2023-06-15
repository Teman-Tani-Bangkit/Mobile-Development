package com.dicoding.temantani.api_settings.response

import com.google.gson.annotations.SerializedName

data class UploadResponse(

	@field:SerializedName("message")
	val message: String? = null,

	@field:SerializedName("status")
	val status: String? = null
)
