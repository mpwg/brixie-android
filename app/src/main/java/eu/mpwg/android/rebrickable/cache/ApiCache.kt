package eu.mpwg.android.rebrickable.cache

import java.security.MessageDigest

/**
 * Main cache manager that coordinates different cache implementations
 */
class ApiCache(
    private val memoryCache: MemoryCache,
    private val configuration: CacheConfiguration
) : CacheProtocol {
    
    constructor(configuration: CacheConfiguration = CacheConfiguration.DEFAULT) : this(
        MemoryCache(configuration),
        configuration
    )
    
    override suspend fun <T> store(key: String, value: T, durationMs: Long?) {
        val cacheKey = generateCacheKey(key)
        memoryCache.store(cacheKey, value, durationMs)
    }
    
    override suspend fun <T> retrieve(key: String, type: Class<T>): T? {
        val cacheKey = generateCacheKey(key)
        return memoryCache.retrieve(cacheKey, type)
    }
    
    override suspend fun exists(key: String): Boolean {
        val cacheKey = generateCacheKey(key)
        return memoryCache.exists(cacheKey)
    }
    
    override suspend fun remove(key: String) {
        val cacheKey = generateCacheKey(key)
        memoryCache.remove(cacheKey)
    }
    
    override suspend fun clearAll() {
        memoryCache.clearAll()
    }
    
    override suspend fun cleanupExpired() {
        memoryCache.cleanupExpired()
    }
    
    /**
     * Generate a cache key from the input string
     */
    private fun generateCacheKey(input: String): String {
        return try {
            val digest = MessageDigest.getInstance("SHA-256")
            val hash = digest.digest(input.toByteArray())
            hash.joinToString("") { "%02x".format(it) }
        } catch (e: Exception) {
            // Fallback to simple key if SHA-256 is not available
            input.replace(Regex("[^a-zA-Z0-9_-]"), "_")
        }
    }
    
    /**
     * Create a cache key for API requests
     */
    fun createRequestCacheKey(
        endpoint: String,
        parameters: Map<String, Any?> = emptyMap()
    ): String {
        val sortedParams = parameters
            .filterValues { it != null }
            .toSortedMap()
            .map { "${it.key}=${it.value}" }
            .joinToString("&")
        
        return if (sortedParams.isEmpty()) {
            endpoint
        } else {
            "$endpoint?$sortedParams"
        }
    }
    
    /**
     * Get cache statistics
     */
    fun getStats(): CacheStats {
        return memoryCache.getStats()
    }
}