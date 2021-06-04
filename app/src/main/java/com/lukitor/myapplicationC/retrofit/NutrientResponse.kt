package com.lukitor.myapplicationC.retrofit

import com.google.gson.annotations.SerializedName

data class NutrientResponse(

	@field:SerializedName("serving_id")
	val servingId: String? = null,

	@field:SerializedName("fiber")
	val fiber: String? = null,

	@field:SerializedName("calcium")
	val calcium: String? = null,

	@field:SerializedName("potassium")
	val potassium: String? = null,

	@field:SerializedName("measurement_description")
	val measurementDescription: String? = null,

	@field:SerializedName("vitamin_a")
	val vitaminA: String? = null,

	@field:SerializedName("vitamin_c")
	val vitaminC: String? = null,

	@field:SerializedName("metric_serving_unit")
	val metricServingUnit: String? = null,

	@field:SerializedName("serving_url")
	val servingUrl: String? = null,

	@field:SerializedName("calories")
	val calories: String? = null,

	@field:SerializedName("saturated_fat")
	val saturatedFat: String? = null,

	@field:SerializedName("carbohydrate")
	val carbohydrate: String? = null,

	@field:SerializedName("metric_serving_amount")
	val metricServingAmount: String? = null,

	@field:SerializedName("sodium")
	val sodium: String? = null,

	@field:SerializedName("monounsaturated_fat")
	val monounsaturatedFat: String? = null,

	@field:SerializedName("polyunsaturated_fat")
	val polyunsaturatedFat: String? = null,

	@field:SerializedName("serving_description")
	val servingDescription: String? = null,

	@field:SerializedName("protein")
	val protein: String? = null,

	@field:SerializedName("fat")
	val fat: String? = null,

	@field:SerializedName("cholesterol")
	val cholesterol: String? = null,

	@field:SerializedName("iron")
	val iron: String? = null,

	@field:SerializedName("sugar")
	val sugar: String? = null,

	@field:SerializedName("number_of_units")
	val numberOfUnits: String? = null
)
