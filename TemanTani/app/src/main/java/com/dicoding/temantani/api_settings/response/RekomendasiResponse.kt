package com.dicoding.temantani.api_settings.response

import com.google.gson.annotations.SerializedName

data class RekomendasiResponse(

	@field:SerializedName("Crops 3")
	val crops3: String? = null,

	@field:SerializedName("Crops 1")
	val crops1: String? = null,

	@field:SerializedName("Crops 2")
	val crops2: String? = null
)
