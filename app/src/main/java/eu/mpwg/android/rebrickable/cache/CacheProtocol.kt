package eu.mpwg.android.rebrickable.cache

/**
 * Protocol defining cache operations
 */
interface CacheProtocol {
    
    /**
     * Store a value in the cache
     */
    suspend fun <T> store(key: String, value: T, durationMs: Long? = null)
    
    /**
     * Retrieve a value from the cache
     */
    suspend fun <T> retrieve(key: String, type: Class<T>): T?
    
    /**
     * Check if a key exists in the cache and is still valid
     */
    suspend fun exists(key: String): Boolean
    
    /**
     * Remove a specific key from the cache
     */
    suspend fun remove(key: String)
    
    /**
     * Clear all cached data
     */
    suspend fun clearAll()
    
    /**
     * Clean up expired cache entries
     */
    suspend fun cleanupExpired()
}

/**
 * Cache entry with expiration
 */
data class CacheEntry<T>(
    val value: T,
    val expirationTime: Long,
    val createdTime: Long = System.currentTimeMillis()
) {
    
    /**
     * Check if this cache entry is still valid
     */
    fun isValid(): Boolean {
        return System.currentTimeMillis() < expirationTime
    }
}