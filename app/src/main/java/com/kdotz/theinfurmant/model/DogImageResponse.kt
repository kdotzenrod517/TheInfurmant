package com.kdotz.theinfurmant.model

import com.google.gson.annotations.SerializedName

data class DogImageResponse (

	@SerializedName("breeds") val breeds : List<Breeds>,
	@SerializedName("height") val height : Int,
	@SerializedName("id") val id : String,
	@SerializedName("url") val url : String,
	@SerializedName("width") val width : Int
)