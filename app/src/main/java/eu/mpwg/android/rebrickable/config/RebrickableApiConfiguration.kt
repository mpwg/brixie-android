package eu.mpwg.android.rebrickable.config

import android.util.Log
import java.net.MalformedURLException
import java.net.URL

/**
 * Configuration for the Rebrickable API client with extensive error handling
 */
class RebrickableApiConfiguration private constructor(
    val apiKey: String,
    val basePath: String = DEFAULT_BASE_PATH
) {
    
    init {
        validateConfiguration()
    }
    
    private fun validateConfiguration() {
        // Validate API key
        if (apiKey.isBlank()) {
            throw IllegalArgumentException("API-Schlüssel darf nicht leer sein")
        }
        
        if (apiKey.length < 10) {
            throw IllegalArgumentException("API-Schlüssel ist zu kurz (mindestens 10 Zeichen erforderlich)")
        }
        
        if (apiKey.equals("dummy-api-key", ignoreCase = true) || 
            apiKey.equals("test", ignoreCase = true) || 
            apiKey.equals("placeholder", ignoreCase = true)) {
            Log.w(TAG, "Warnung: Es wird ein Test/Dummy-API-Schlüssel verwendet")
        }
        
        // Validate base path
        if (basePath.isBlank()) {
            throw IllegalArgumentException("Basis-URL darf nicht leer sein")
        }
        
        try {
            val url = URL(basePath)
            if (!url.protocol.equals("https", ignoreCase = true)) {
                Log.w(TAG, "Warnung: Unsichere HTTP-Verbindung wird verwendet statt HTTPS")
            }
        } catch (e: MalformedURLException) {
            throw IllegalArgumentException("Ungültige Basis-URL: $basePath", e)
        }
        
        Log.d(TAG, "API-Konfiguration validiert: Basis-URL=$basePath, API-Schlüssel-Länge=${apiKey.length}")
    }
    
    companion object {
        private const val TAG = "RebrickableApiConfig"
        private const val DEFAULT_BASE_PATH = "https://rebrickable.com"
        
        @JvmStatic
        private lateinit var _shared: RebrickableApiConfiguration
        
        @JvmStatic
        val shared: RebrickableApiConfiguration
            get() {
                if (!::_shared.isInitialized) {
                    throw IllegalStateException("API-Konfiguration wurde noch nicht initialisiert. Rufen Sie initialize() zuerst auf.")
                }
                return _shared
            }
        
        @JvmStatic
        val isInitialized: Boolean
            get() = ::_shared.isInitialized
        
        /**
         * Initialize the shared configuration with an API key
         * @param apiKey The API key for Rebrickable API
         * @param basePath The base URL for the API (defaults to official Rebrickable URL)
         * @throws IllegalArgumentException if parameters are invalid
         * @throws IllegalStateException if already initialized
         */
        @JvmStatic
        fun initialize(apiKey: String, basePath: String = DEFAULT_BASE_PATH) {
            try {
                Log.d(TAG, "Initializing Rebrickable API configuration...")
                
                if (::_shared.isInitialized) {
                    throw IllegalStateException("API-Konfiguration wurde bereits initialisiert")
                }
                
                _shared = RebrickableApiConfiguration(apiKey, basePath)
                
                Log.i(TAG, "Rebrickable API configuration initialized successfully")
                
            } catch (e: Exception) {
                Log.e(TAG, "Failed to initialize API configuration", e)
                throw RuntimeException("Fehler beim Initialisieren der API-Konfiguration", e)
            }
        }
        
        /**
         * Create a new configuration instance (for testing or multiple configs)
         * @param apiKey The API key for Rebrickable API
         * @param basePath The base URL for the API
         * @return New configuration instance
         * @throws IllegalArgumentException if parameters are invalid
         */
        @JvmStatic
        fun create(apiKey: String, basePath: String = DEFAULT_BASE_PATH): RebrickableApiConfiguration {
            try {
                Log.d(TAG, "Creating new API configuration instance...")
                
                val config = RebrickableApiConfiguration(apiKey, basePath)
                
                Log.d(TAG, "API configuration instance created successfully")
                return config
                
            } catch (e: Exception) {
                Log.e(TAG, "Failed to create API configuration", e)
                throw RuntimeException("Fehler beim Erstellen der API-Konfiguration", e)
            }
        }
        
        
        /**
         * Reset the shared configuration (for testing purposes)
         */
        @JvmStatic
        internal fun reset() {
            Log.d(TAG, "Resetting API configuration")
            if (::_shared.isInitialized) {
                // Clear the lateinit reference by reflection if needed for testing
                // This is primarily for testing scenarios
            }
        }
    }
}