package eu.mpwg.android.brixie.di

import android.content.Context
import androidx.room.Room
import eu.mpwg.android.brixie.data.local.BrixieDatabase
import eu.mpwg.android.brixie.data.repository.SetRepository
import eu.mpwg.android.brixie.data.repository.ThemeRepository
import eu.mpwg.android.rebrickable.api.RebrickableApiClient
import eu.mpwg.android.rebrickable.config.RebrickableApiConfiguration

interface AppContainer {
    val setRepository: SetRepository
    val themeRepository: ThemeRepository
}

class DefaultAppContainer(private val context: Context) : AppContainer {
    
    private val apiConfiguration by lazy {
        RebrickableApiConfiguration.create("dummy-api-key") // TODO: Replace with real API key
    }
    
    private val apiClient by lazy {
        RebrickableApiClient.create(apiConfiguration)
    }
    
    private val database by lazy {
        Room.databaseBuilder(
            context,
            BrixieDatabase::class.java,
            "brixie_database"
        ).build()
    }
    
    override val setRepository: SetRepository by lazy {
        SetRepository(apiClient, database.setDao())
    }
    
    override val themeRepository: ThemeRepository by lazy {
        ThemeRepository(apiClient, database.themeDao())
    }
}