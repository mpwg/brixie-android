package eu.mpwg.android.rebrickable.cache

/**
 * Configuration for API caching
 */
data class CacheConfiguration(
    /**
     * Whether caching is enabled
     */
    val isEnabled: Boolean = true,
    
    /**
     * Default cache duration in milliseconds (1 hour by default)
     */
    val defaultCacheDurationMs: Long = 60 * 60 * 1000,
    
    /**
     * Maximum cache size in bytes (10 MB by default)
     */
    val maxCacheSizeBytes: Long = 10 * 1024 * 1024,
    
    /**
     * Cache cleanup interval in milliseconds (1 day by default)
     */
    val cleanupIntervalMs: Long = 24 * 60 * 60 * 1000
) {
    
    companion object {
        /**
         * Default cache configuration
         */
        @JvmStatic
        val DEFAULT = CacheConfiguration()
        
        /**
         * Disabled cache configuration
         */
        @JvmStatic
        val DISABLED = CacheConfiguration(isEnabled = false)
        
        /**
         * Memory-only cache configuration (short duration)
         */
        @JvmStatic
        val MEMORY_ONLY = CacheConfiguration(
            defaultCacheDurationMs = 5 * 60 * 1000 // 5 minutes
        )
        
        /**
         * Long-term cache configuration (1 day)
         */
        @JvmStatic
        val LONG_TERM = CacheConfiguration(
            defaultCacheDurationMs = 24 * 60 * 60 * 1000 // 1 day
        )
    }
}