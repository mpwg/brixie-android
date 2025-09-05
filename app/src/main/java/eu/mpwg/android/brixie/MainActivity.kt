package eu.mpwg.android.brixie

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import eu.mpwg.android.brixie.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            Log.i(TAG, "Initializing MainActivity...")
            
            // Check if application was initialized properly
            checkApplicationInitialization()
            
            // Initialize view binding
            initializeViewBinding()
            
            // Setup navigation
            setupNavigation()
            
            Log.i(TAG, "MainActivity initialized successfully")
            
        } catch (e: Exception) {
            val errorMessage = "Fehler beim Laden der Hauptaktivität: ${e.javaClass.simpleName}: ${e.message}"
            Log.e(TAG, errorMessage, e)
            
            showCriticalErrorDialog(errorMessage, e)
        }
    }
    
    private fun checkApplicationInitialization() {
        try {
            val app = application as? BrixieApplication
                ?: throw IllegalStateException("Application ist nicht vom Typ BrixieApplication")
            
            if (!app.isInitialized()) {
                val errorDetails = app.initializationError ?: "Unbekannter Initialisierungsfehler"
                throw RuntimeException("Application wurde nicht ordnungsgemäß initialisiert: $errorDetails")
            }
            
            Log.d(TAG, "Application initialization check passed")
            
        } catch (e: Exception) {
            throw RuntimeException("Fehler bei der Überprüfung der Anwendungsinitialisierung", e)
        }
    }
    
    private fun initializeViewBinding() {
        try {
            Log.d(TAG, "Initializing view binding...")
            
            binding = ActivityMainBinding.inflate(layoutInflater)
            setContentView(binding.root)
            
            Log.d(TAG, "View binding initialized successfully")
            
        } catch (e: Exception) {
            throw RuntimeException("Fehler beim Initialisieren des View Bindings", e)
        }
    }
    
    private fun setupNavigation() {
        try {
            Log.d(TAG, "Setting up navigation...")
            
            val navView: BottomNavigationView = binding.navView
            
            val navController = findNavController(R.id.nav_host_fragment_activity_main)
            
            // Setup bottom navigation with nav controller
            // No ActionBar setup needed since we're using NoActionBar theme
            navView.setupWithNavController(navController)
            
            Log.d(TAG, "Navigation setup completed successfully")
            
        } catch (e: Exception) {
            throw RuntimeException("Fehler beim Einrichten der Navigation", e)
        }
    }
    
    private fun showCriticalErrorDialog(message: String, exception: Throwable) {
        try {
            // Show toast for immediate feedback
            Toast.makeText(this, "Kritischer Fehler aufgetreten", Toast.LENGTH_SHORT).show()
            
            val detailedMessage = buildString {
                appendLine("Es ist ein kritischer Fehler aufgetreten:")
                appendLine()
                appendLine("Fehlermeldung:")
                appendLine(message)
                appendLine()
                appendLine("Technische Details:")
                appendLine("Fehlertyp: ${exception.javaClass.simpleName}")
                appendLine("Ursache: ${exception.cause?.message ?: "Unbekannt"}")
                appendLine()
                appendLine("Bitte starten Sie die App neu. Falls das Problem weiterhin besteht, kontaktieren Sie den Support.")
            }
            
            AlertDialog.Builder(this)
                .setTitle("Kritischer Fehler")
                .setMessage(detailedMessage)
                .setPositiveButton("App beenden") { _, _ ->
                    Log.w(TAG, "User chose to exit app due to critical error")
                    finishAffinity()
                }
                .setNegativeButton("Trotzdem fortfahren") { dialog, _ ->
                    Log.w(TAG, "User chose to continue despite critical error")
                    dialog.dismiss()
                }
                .setCancelable(false)
                .show()
                
        } catch (dialogException: Exception) {
            Log.e(TAG, "Failed to show error dialog", dialogException)
            // Fallback: finish activity if we can't show dialog
            finish()
        }
    }
    
    companion object {
        private const val TAG = "MainActivity"
    }
}