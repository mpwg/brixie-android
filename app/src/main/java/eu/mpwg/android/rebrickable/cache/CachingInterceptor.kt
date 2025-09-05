package eu.mpwg.android.rebrickable.cache

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import java.lang.reflect.Type

/**
 * OkHttp interceptor that handles API response caching
 */
class CachingInterceptor(
    private val cache: ApiCache,
    private val gson: Gson = Gson()
) : Interceptor {
    
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        
        // Only cache GET requests
        if (request.method != "GET") {
            return chain.proceed(request)
        }
        
        val cacheKey = cache.createRequestCacheKey(
            endpoint = request.url.encodedPath,
            parameters = request.url.queryParameterNames.associateWith { 
                request.url.queryParameter(it)
            }
        )
        
        // Try to get from cache first
        try {
            // Use runBlocking for synchronous cache access in interceptor
            val cachedResponse = runBlocking {
                cache.retrieve(cacheKey, String::class.java)
            }
            if (cachedResponse != null) {
                return createCachedResponse(request, cachedResponse)
            }
        } catch (e: Exception) {
            // Continue with network request if cache fails
        }
        
        // Make network request
        val response = chain.proceed(request)
        
        // Cache successful responses
        if (response.isSuccessful) {
            try {
                val responseBody = response.body
                val responseString = responseBody?.string()
                
                if (responseString != null) {
                    // Store in cache using runBlocking
                    runBlocking {
                        cache.store(cacheKey, responseString)
                    }
                    
                    // Recreate response with new body
                    return response.newBuilder()
                        .body(responseString.toResponseBody(responseBody.contentType()))
                        .build()
                }
            } catch (e: Exception) {
                // Return original response if caching fails
                return response
            }
        }
        
        return response
    }
    
    private fun createCachedResponse(request: okhttp3.Request, cachedData: String): Response {
        return Response.Builder()
            .request(request)
            .protocol(okhttp3.Protocol.HTTP_1_1)
            .code(200)
            .message("OK")
            .header("Cache-Control", "max-age=3600")
            .header("X-Cached", "true")
            .body(cachedData.toResponseBody("application/json".toMediaType()))
            .build()
    }
}

/**
 * Extension function to easily add caching to API calls
 */
suspend inline fun <reified T> ApiCache.cacheApiCall(
    key: String,
    crossinline apiCall: suspend () -> T,
    durationMs: Long? = null
): T {
    // Try cache first
    val cached = retrieve(key, T::class.java)
    if (cached != null) {
        return cached
    }
    
    // Make API call and cache result
    val result = apiCall()
    store(key, result, durationMs)
    return result
}