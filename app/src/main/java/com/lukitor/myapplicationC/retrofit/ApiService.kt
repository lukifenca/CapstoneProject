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
//    @GET("3/discover/movie")
//    fun getMovies(
//        @Query("api_key") api_key: String = "d41df99ac455e611fbf59daae71c5bf3",
//        @Query("language") language:String = "en-US",
//        @Query("sort_by") sort_by:String = "popularity.desc",
//        @Query("include_adult") include_adult:String = "false",
//        @Query("vote_average.gte") param:Int = 8,
//        @Query("release_date.gte") parameter:String = "2018-01-01"
//    ): Call<ListMoviesResponse>
}