package eu.mpwg.android.rebrickable.config

import android.util.Log
import java.net.MalformedURLException
import java.net.URL

/**
 * Logger interface to allow testing without Android dependencies
 */
interface Logger {
    fun d(tag: String, message: String): Unit
    fun i(tag: String, message: String): Unit
    fun w(tag: String, message: String): Unit
    fun e(tag: String, message: String, throwable: Throwable? = null): Unit
}

/**
 * Android Log implementation
 */
internal class AndroidLogger : Logger {
    override fun d(tag: String, message: String): Unit { Log.d(tag, message) }
    override fun i(tag: String, message: String): Unit { Log.i(tag, message) }
    override fun w(tag: String, message: String): Unit { Log.w(tag, message) }
    override fun e(tag: String, message: String, throwable: Throwable?): Unit {
        if (throwable != null) {
            Log.e(tag, message, throwable)
        } else {
            Log.e(tag, message)
        }
    }
}

/**
 * Test-friendly logger that uses println
 */
internal class TestLogger : Logger {
    override fun d(tag: String, message: String): Unit { println("D/$tag: $message") }
    override fun i(tag: String, message: String): Unit { println("I/$tag: $message") }
    override fun w(tag: String, message: String): Unit { println("W/$tag: $message") }
    override fun e(tag: String, message: String, throwable: Throwable?): Unit {
        println("E/$tag: $message")
        throwable?.printStackTrace()
    }
}

/**
 * Configuration for the Rebrickable API client with extensive error handling
 */
class RebrickableApiConfiguration private constructor(
    val apiKey: String,
    val basePath: String = DEFAULT_BASE_PATH,
    private val logger: Logger = AndroidLogger()
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
            logger.w(TAG, "Warnung: Es wird ein Test/Dummy-API-Schlüssel verwendet")
        }
        
        // Validate base path
        if (basePath.isBlank()) {
            throw IllegalArgumentException("Basis-URL darf nicht leer sein")
        }
        
        try {
            val url = URL(basePath)
            if (!url.protocol.equals("https", ignoreCase = true)) {
                logger.w(TAG, "Warnung: Unsichere HTTP-Verbindung wird verwendet statt HTTPS")
            }
        } catch (e: MalformedURLException) {
            throw IllegalArgumentException("Ungültige Basis-URL: $basePath", e)
        }
        
        logger.d(TAG, "API-Konfiguration validiert: Basis-URL=$basePath, API-Schlüssel-Länge=${apiKey.length}")
    }
    
    companion object {
        private const val TAG = "RebrickableApiConfig"
        private const val DEFAULT_BASE_PATH = "https://rebrickable.com"
        
        @JvmStatic
        private var _shared: RebrickableApiConfiguration? = null
        
        @JvmStatic
        val shared: RebrickableApiConfiguration
            get() {
                return _shared ?: throw IllegalStateException("API-Konfiguration wurde noch nicht initialisiert. Rufen Sie initialize() zuerst auf.")
            }
        
        @JvmStatic
        val isInitialized: Boolean
            get() = _shared != null
        
        /**
         * Initialize the shared configuration with an API key
         * @param apiKey The API key for Rebrickable API
         * @param basePath The base URL for the API (defaults to official Rebrickable URL)
         * @throws IllegalArgumentException if parameters are invalid
         * @throws IllegalStateException if already initialized
         */
        @JvmStatic
        fun initialize(apiKey: String, basePath: String = DEFAULT_BASE_PATH, logger: Logger = AndroidLogger()) {
            try {
                logger.d(TAG, "Initializing Rebrickable API configuration...")
                
                if (_shared != null) {
                    throw IllegalStateException("API-Konfiguration wurde bereits initialisiert")
                }
                
                _shared = RebrickableApiConfiguration(apiKey, basePath, logger)
                
                logger.i(TAG, "Rebrickable API configuration initialized successfully")
                
            } catch (e: Exception) {
                logger.e(TAG, "Failed to initialize API configuration", e)
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
        fun create(apiKey: String, basePath: String = DEFAULT_BASE_PATH, logger: Logger = TestLogger()): RebrickableApiConfiguration {
            try {
                logger.d(TAG, "Creating new API configuration instance...")
                
                val config = RebrickableApiConfiguration(apiKey, basePath, logger)
                
                logger.d(TAG, "API configuration instance created successfully")
                return config
                
            } catch (e: Exception) {
                logger.e(TAG, "Failed to create API configuration", e)
                throw RuntimeException("Fehler beim Erstellen der API-Konfiguration", e)
            }
        }
        
        
        /**
         * Reset the shared configuration (for testing purposes)
         */
        @JvmStatic
        internal fun reset() {
            val logger = TestLogger()
            logger.d(TAG, "Resetting API configuration")
            _shared = null
        }
    }
}