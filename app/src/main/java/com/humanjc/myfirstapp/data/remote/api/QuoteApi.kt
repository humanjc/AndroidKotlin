package com.humanjc.myfirstapp.data.remote.api

import com.humanjc.myfirstapp.data.remote.model.QuoteResponse
import retrofit2.http.GET

interface QuoteApi {
    @GET("advice")
    suspend fun getRandomAdvice(): QuoteResponse
}
