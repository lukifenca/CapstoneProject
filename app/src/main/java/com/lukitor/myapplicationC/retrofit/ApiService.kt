package com.lukitor.myapplicationC.retrofit

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @Multipart
    @POST("uploadimage.php")//endpoint
    fun uploadImage(
        @Part image: MultipartBody.Part
    ):Call<ResponseApiModel>

    @GET("food/{param}")
    fun getNutrients(
        @Path("param") param:String
    ): Call<NutrientResponses>
}