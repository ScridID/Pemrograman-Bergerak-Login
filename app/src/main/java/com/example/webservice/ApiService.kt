package com.example.webservice

import android.R
import android.os.Message
import retrofit2.call
import retrofit2.http.body
import retrofit2.http.GET
import retrofit2.http.POST


interface ApiService {
    @GET("api/data")
    fun getData():Call<ApiResponse>

    @POST("api/data")
    fun sendData(@Body request: Postrequest): Call<ApiResponse>
}
data class ApiResponse(
    val message: String,
    val data: List<String>? = null
)

data class Postrequest(
    val name: String
)
