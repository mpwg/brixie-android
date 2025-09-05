package eu.mpwg.android.brixie

import android.app.Application
import eu.mpwg.android.brixie.di.AppContainer
import eu.mpwg.android.brixie.di.DefaultAppContainer
import eu.mpwg.android.rebrickable.config.RebrickableApiConfiguration

class BrixieApplication : Application() {
    
    lateinit var container: AppContainer
    
    override fun onCreate() {
        super.onCreate()
        
        // Initialize the Rebrickable API configuration
        // TODO: Replace with actual API key from secure storage or build config
        RebrickableApiConfiguration.initialize("dummy-api-key")
        
        // Initialize dependency container
        container = DefaultAppContainer(this)
    }
}