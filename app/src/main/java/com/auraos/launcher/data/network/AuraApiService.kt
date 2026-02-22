package com.auraos.launcher.data.network

import com.auraos.launcher.data.model.IntentResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Retrofit API interface for the local Python (Flask/FastAPI) mock server
 * running at localhost:5000.
 */
interface AuraApiService {

    /**
     * Fetch a mock intent response for a given keyword.
     * GET http://10.0.2.2:5000/intent?keyword=pizza
     */
    @GET("intent")
    suspend fun getIntentResponse(
        @Query("keyword") keyword: String
    ): Response<IntentResponse>

    /**
     * Health-check endpoint.
     * GET http://10.0.2.2:5000/ping
     */
    @GET("ping")
    suspend fun ping(): Response<Map<String, String>>
}
