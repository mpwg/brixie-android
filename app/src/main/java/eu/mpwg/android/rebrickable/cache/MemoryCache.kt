package eu.mpwg.android.rebrickable.cache

import java.util.concurrent.ConcurrentHashMap

/**
 * Simple in-memory cache implementation
 */
class MemoryCache(
    private val configuration: CacheConfiguration
) : CacheProtocol {
    
    private val cache = ConcurrentHashMap<String, CacheEntry<Any>>()
    
    override suspend fun <T> store(key: String, value: T, durationMs: Long?) {
        if (!configuration.isEnabled) return
        
        val duration = durationMs ?: configuration.defaultCacheDurationMs
        val expirationTime = System.currentTimeMillis() + duration
        
        cache[key] = CacheEntry(value as Any, expirationTime)
        
        // Cleanup expired entries periodically
        if (cache.size % 100 == 0) {
            cleanupExpired()
        }
    }
    
    @Suppress("UNCHECKED_CAST")
    override suspend fun <T> retrieve(key: String, type: Class<T>): T? {
        if (!configuration.isEnabled) return null
        
        val entry = cache[key] ?: return null
        
        if (!entry.isValid()) {
            cache.remove(key)
            return null
        }
        
        return try {
            entry.value as T
        } catch (e: ClassCastException) {
            cache.remove(key)
            null
        }
    }
    
    override suspend fun exists(key: String): Boolean {
        if (!configuration.isEnabled) return false
        
        val entry = cache[key] ?: return false
        
        if (!entry.isValid()) {
            cache.remove(key)
            return false
        }
        
        return true
    }
    
    override suspend fun remove(key: String) {
        cache.remove(key)
    }
    
    override suspend fun clearAll() {
        cache.clear()
    }
    
    override suspend fun cleanupExpired() {
        val expiredKeys = cache.entries
            .filter { !it.value.isValid() }
            .map { it.key }
        
        expiredKeys.forEach { cache.remove(it) }
    }
    
    /**
     * Get current cache size
     */
    fun size(): Int = cache.size
    
    /**
     * Get cache hit statistics (for debugging)
     */
    fun getStats(): CacheStats {
        val entries = cache.values
        val validEntries = entries.count { it.isValid() }
        val expiredEntries = entries.size - validEntries
        
        return CacheStats(
            totalEntries = entries.size,
            validEntries = validEntries,
            expiredEntries = expiredEntries
        )
    }
}

/**
 * Cache statistics for monitoring
 */
data class CacheStats(
    val totalEntries: Int,
    val validEntries: Int,
    val expiredEntries: Int
)