package com.auraos.launcher.data.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Singleton Retrofit client configured to talk to the local Python server.
 *
 * Important: On the Android Emulator, "localhost" maps to the host machine
 * via 10.0.2.2. On a real device, replace with your machine's local IP.
 */
object RetrofitClient {

    // Emulator: 10.0.2.2, Physical device: your PC's LAN IP (e.g. 192.168.1.x)
    private const val BASE_URL = "http://10.0.2.2:5000/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(loggingInterceptor)
        .connectTimeout(10, TimeUnit.SECONDS)
        .readTimeout(15, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: AuraApiService by lazy {
        retrofit.create(AuraApiService::class.java)
    }
}
