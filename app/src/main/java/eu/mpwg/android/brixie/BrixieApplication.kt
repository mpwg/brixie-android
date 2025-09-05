package eu.mpwg.android.brixie

import android.app.Application
import android.util.Log
import android.widget.Toast
import eu.mpwg.android.brixie.di.AppContainer
import eu.mpwg.android.brixie.di.DefaultAppContainer
import eu.mpwg.android.rebrickable.config.RebrickableApiConfiguration

class BrixieApplication : Application() {
    
    lateinit var container: AppContainer
        private set
    
    var initializationError: String? = null
        private set
    
    override fun onCreate() {
        super.onCreate()
        
        try {
            Log.i(TAG, "Initializing Brixie Application...")
            
            // Initialize the Rebrickable API configuration
            initializeApiConfiguration()
            
            // Initialize dependency container
            initializeDependencyContainer()
            
            Log.i(TAG, "Brixie Application initialized successfully")
            
        } catch (e: Exception) {
            val errorMessage = "Kritischer Fehler beim Start der Anwendung: ${e.javaClass.simpleName}: ${e.message}"
            Log.e(TAG, errorMessage, e)
            initializationError = errorMessage
            
            // Show error to user
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
            
            // Re-throw to ensure the app crashes properly with logs
            throw RuntimeException("Application initialization failed", e)
        }
    }
    
    private fun initializeApiConfiguration() {
        try {
            Log.d(TAG, "Initializing Rebrickable API configuration...")
            
            // TODO: Replace with actual API key from secure storage or build config
            val apiKey = "dummy-api-key"
            
            if (apiKey.isBlank()) {
                throw IllegalStateException("API-Schlüssel ist leer oder nicht gesetzt")
            }
            
            RebrickableApiConfiguration.initialize(apiKey)
            Log.d(TAG, "Rebrickable API configuration initialized successfully")
            
        } catch (e: Exception) {
            throw RuntimeException("Fehler beim Initialisieren der API-Konfiguration", e)
        }
    }
    
    private fun initializeDependencyContainer() {
        try {
            Log.d(TAG, "Initializing dependency injection container...")
            
            container = DefaultAppContainer(this)
            Log.d(TAG, "Dependency injection container initialized successfully")
            
        } catch (e: Exception) {
            throw RuntimeException("Fehler beim Initialisieren des Dependency-Containers", e)
        }
    }
    
    fun isInitialized(): Boolean {
        return ::container.isInitialized && initializationError == null
    }
    
    companion object {
        private const val TAG = "BrixieApplication"
    }
}