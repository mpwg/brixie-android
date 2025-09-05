package eu.mpwg.android.brixie.di

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
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
        try {
            Log.d(TAG, "Retrieving API configuration...")
            
            if (!RebrickableApiConfiguration.isInitialized) {
                throw IllegalStateException("API-Konfiguration wurde nicht initialisiert")
            }
            
            val config = RebrickableApiConfiguration.shared
            Log.d(TAG, "API configuration retrieved successfully")
            config
            
        } catch (e: Exception) {
            Log.e(TAG, "Failed to retrieve API configuration", e)
            throw RuntimeException("Fehler beim Abrufen der API-Konfiguration", e)
        }
    }
    
    private val apiClient by lazy {
        try {
            Log.d(TAG, "Creating API client...")
            
            val client = RebrickableApiClient.create(apiConfiguration)
            Log.d(TAG, "API client created successfully")
            client
            
        } catch (e: Exception) {
            Log.e(TAG, "Failed to create API client", e)
            throw RuntimeException("Fehler beim Erstellen des API-Clients", e)
        }
    }
    
    private val database by lazy {
        try {
            Log.d(TAG, "Initializing database...")
            
            // Verify context is not null and valid
            if (context.applicationContext == null) {
                throw IllegalStateException("Ungültiger Anwendungskontext")
            }
            
            val db = Room.databaseBuilder(
                context.applicationContext,
                BrixieDatabase::class.java,
                DATABASE_NAME
            )
            .addCallback(databaseCallback)
            .fallbackToDestructiveMigration(dropAllTables = true) // For development - remove in production
            .build()
            
            Log.d(TAG, "Database initialized successfully")
            db
            
        } catch (e: Exception) {
            Log.e(TAG, "Failed to initialize database", e)
            throw RuntimeException("Fehler beim Initialisieren der Datenbank: ${e.message}", e)
        }
    }
    
    private val databaseCallback = object : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            Log.i(TAG, "Database created successfully")
        }
        
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            Log.d(TAG, "Database opened successfully")
        }
        
        override fun onDestructiveMigration(db: SupportSQLiteDatabase) {
            super.onDestructiveMigration(db)
            Log.w(TAG, "Database destructive migration performed")
        }
    }
    
    override val setRepository: SetRepository by lazy {
        try {
            Log.d(TAG, "Creating SetRepository...")
            
            val dao = database.setDao()
            val repository = SetRepository(apiClient, dao)
            
            Log.d(TAG, "SetRepository created successfully")
            repository
            
        } catch (e: Exception) {
            Log.e(TAG, "Failed to create SetRepository", e)
            throw RuntimeException("Fehler beim Erstellen des Set-Repositorys", e)
        }
    }
    
    override val themeRepository: ThemeRepository by lazy {
        try {
            Log.d(TAG, "Creating ThemeRepository...")
            
            val dao = database.themeDao()
            val repository = ThemeRepository(apiClient, dao)
            
            Log.d(TAG, "ThemeRepository created successfully")
            repository
            
        } catch (e: Exception) {
            Log.e(TAG, "Failed to create ThemeRepository", e)
            throw RuntimeException("Fehler beim Erstellen des Theme-Repositorys", e)
        }
    }
    
    companion object {
        private const val TAG = "DefaultAppContainer"
        private const val DATABASE_NAME = "brixie_database"
    }
}