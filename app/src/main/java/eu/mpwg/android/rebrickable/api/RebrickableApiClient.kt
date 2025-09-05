package eu.mpwg.android.rebrickable.api

import eu.mpwg.android.rebrickable.config.RebrickableApiConfiguration
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Main API client for Rebrickable
 */
class RebrickableApiClient private constructor(
    private val configuration: RebrickableApiConfiguration
) {
    
    companion object {
        /**
         * Create a client using the shared configuration
         */
        @JvmStatic
        fun create(): RebrickableApiClient {
            return RebrickableApiClient(RebrickableApiConfiguration.shared)
        }
        
        /**
         * Create a client with a custom configuration
         */
        @JvmStatic
        fun create(configuration: RebrickableApiConfiguration): RebrickableApiClient {
            return RebrickableApiClient(configuration)
        }
    }
    
    private val authInterceptor = Interceptor { chain ->
        val original = chain.request()
        val requestBuilder = original.newBuilder()
            .header("authorization", "key ${configuration.apiKey}")
        chain.proceed(requestBuilder.build())
    }
    
    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BASIC
    }
    
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(loggingInterceptor)
        .build()
    
    private val retrofit = Retrofit.Builder()
        .baseUrl(configuration.basePath)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    
    /**
     * Get the Lego API service
     */
    val legoApi: LegoApi = retrofit.create(LegoApi::class.java)
}