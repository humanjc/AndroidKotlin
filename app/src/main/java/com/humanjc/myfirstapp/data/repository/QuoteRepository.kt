package com.humanjc.myfirstapp.data.repository

import com.humanjc.myfirstapp.data.remote.api.QuoteApi
import com.humanjc.myfirstapp.data.remote.model.QuoteResponse
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class QuoteRepository @Inject constructor(
    private val quoteApi: QuoteApi
) {
    suspend fun getRandomQuote(): Result<QuoteResponse> {
        return try {
            val response = quoteApi.getRandomAdvice()
            Result.success(response)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
