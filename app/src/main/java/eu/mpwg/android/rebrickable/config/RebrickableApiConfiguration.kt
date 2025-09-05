package eu.mpwg.android.rebrickable.config

/**
 * Configuration for the Rebrickable API client
 */
class RebrickableApiConfiguration private constructor(
    val apiKey: String,
    val basePath: String = DEFAULT_BASE_PATH
) {
    
    companion object {
        private const val DEFAULT_BASE_PATH = "https://rebrickable.com"
        
        @JvmStatic
        lateinit var shared: RebrickableApiConfiguration
            private set
        
        /**
         * Initialize the shared configuration with an API key
         */
        @JvmStatic
        fun initialize(apiKey: String, basePath: String = DEFAULT_BASE_PATH) {
            shared = RebrickableApiConfiguration(apiKey, basePath)
        }
        
        /**
         * Create a new configuration instance
         */
        @JvmStatic
        fun create(apiKey: String, basePath: String = DEFAULT_BASE_PATH): RebrickableApiConfiguration {
            return RebrickableApiConfiguration(apiKey, basePath)
        }
    }
}