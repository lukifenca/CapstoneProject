package com.lukitor.myapplicationC.retrofit

import com.google.gson.annotations.SerializedName

data class ResponseApiModel(
    @SerializedName("kode")
    var kode: String? = null,
    @SerializedName("pesan")
    var pesan: String? = null

)
