package com.kdotz.theinfurmant.model

import com.google.gson.annotations.SerializedName

data class Breeds (

	@SerializedName("bred_for") val bred_for : String,
	@SerializedName("breed_group") val breed_group : String,
	@SerializedName("height") val height : Height,
	@SerializedName("id") val id : Int,
	@SerializedName("life_span") val life_span : String,
	@SerializedName("name") val name : String,
	@SerializedName("origin") val origin : String,
	@SerializedName("temperament") val temperament : String,
	@SerializedName("weight") val weight : Weight
)