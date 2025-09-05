package eu.mpwg.android.brixie.util

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import java.io.PrintWriter
import java.io.StringWriter
import java.text.SimpleDateFormat
import java.util.*

/**
 * Centralized error reporting utility for the Brixie app
 * Provides detailed error information in German for user-facing messages
 */
object ErrorReporter {
    
    private const val TAG = "ErrorReporter"
    
    /**
     * Report a critical startup error with detailed information
     */
    fun reportStartupError(
        context: Context?,
        phase: String,
        exception: Throwable,
        additionalInfo: Map<String, String> = emptyMap()
    ): String {
        val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.GERMAN).format(Date())
        
        val errorReport = buildString {
            appendLine("=== BRIXIE STARTUP FEHLER ===")
            appendLine("Zeitpunkt: $timestamp")
            appendLine("Phase: $phase")
            appendLine()
            
            // App info
            appendLine("=== APP INFORMATION ===")
            try {
                if (context != null) {
                    val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
                    appendLine("App Version: ${packageInfo.versionName} (${packageInfo.longVersionCode})")
                    appendLine("Paket Name: ${context.packageName}")
                } else {
                    appendLine("App Version: Unbekannt (Kontext nicht verfügbar)")
                }
            } catch (e: PackageManager.NameNotFoundException) {
                appendLine("App Version: Fehler beim Abrufen der Versionsinformationen")
            }
            appendLine()
            
            // Device info
            appendLine("=== GERÄTEINFORMATIONEN ===")
            appendLine("Android Version: ${Build.VERSION.RELEASE} (API ${Build.VERSION.SDK_INT})")
            appendLine("Gerät: ${Build.MANUFACTURER} ${Build.MODEL}")
            appendLine("Board: ${Build.BOARD}")
            appendLine("Hardware: ${Build.HARDWARE}")
            appendLine("ABI: ${Build.SUPPORTED_ABIS.joinToString(", ")}")
            appendLine()
            
            // Memory info
            appendLine("=== SPEICHERINFORMATIONEN ===")
            val runtime = Runtime.getRuntime()
            val totalMemory = runtime.totalMemory() / 1024 / 1024
            val freeMemory = runtime.freeMemory() / 1024 / 1024
            val maxMemory = runtime.maxMemory() / 1024 / 1024
            appendLine("Gesamtspeicher: ${totalMemory}MB")
            appendLine("Freier Speicher: ${freeMemory}MB")
            appendLine("Max. Speicher: ${maxMemory}MB")
            appendLine()
            
            // Additional info
            if (additionalInfo.isNotEmpty()) {
                appendLine("=== ZUSÄTZLICHE INFORMATIONEN ===")
                additionalInfo.forEach { (key, value) ->
                    appendLine("$key: $value")
                }
                appendLine()
            }
            
            // Exception details
            appendLine("=== FEHLERDETAILS ===")
            appendLine("Hauptfehler: ${exception.javaClass.simpleName}")
            appendLine("Fehlermeldung: ${exception.message}")
            
            // Cause chain
            var cause = exception.cause
            var level = 1
            while (cause != null && level <= 5) {
                appendLine("Ursache $level: ${cause.javaClass.simpleName}: ${cause.message}")
                cause = cause.cause
                level++
            }
            appendLine()
            
            // Stack trace
            appendLine("=== STACK TRACE ===")
            val stringWriter = StringWriter()
            exception.printStackTrace(PrintWriter(stringWriter))
            appendLine(stringWriter.toString())
            
            appendLine("=== ENDE DES FEHLERBERICHTS ===")
        }
        
        Log.e(TAG, errorReport)
        return errorReport
    }
    
    /**
     * Get a user-friendly error message in German
     */
    fun getUserFriendlyMessage(exception: Throwable, context: String = ""): String {
        return when (exception) {
            is IllegalArgumentException -> {
                "Ungültige Parameter: ${exception.message}"
            }
            is IllegalStateException -> {
                "Ungültiger Anwendungszustand: ${exception.message}"
            }
            is RuntimeException -> {
                when {
                    exception.message?.contains("API", ignoreCase = true) == true -> 
                        "Problem mit der API-Verbindung: ${exception.message}"
                    exception.message?.contains("database", ignoreCase = true) == true ||
                    exception.message?.contains("Datenbank", ignoreCase = true) == true -> 
                        "Datenbankfehler: ${exception.message}"
                    else -> "Laufzeitfehler: ${exception.message}"
                }
            }
            is OutOfMemoryError -> {
                "Nicht genügend Speicher verfügbar. Bitte starten Sie die App neu."
            }
            is SecurityException -> {
                "Sicherheitsfehler: Fehlende Berechtigungen oder unsichere Operation"
            }
            else -> {
                val contextInfo = if (context.isNotEmpty()) "$context: " else ""
                "$contextInfo${exception.javaClass.simpleName}: ${exception.message}"
            }
        }
    }
    
    /**
     * Get recovery suggestions for common errors
     */
    fun getRecoverySuggestions(exception: Throwable): List<String> {
        return when (exception) {
            is OutOfMemoryError -> listOf(
                "Starten Sie die App neu",
                "Schließen Sie andere Apps",
                "Starten Sie das Gerät neu"
            )
            is SecurityException -> listOf(
                "Überprüfen Sie die App-Berechtigungen",
                "Installieren Sie die App neu"
            )
            is RuntimeException -> {
                when {
                    exception.message?.contains("API", ignoreCase = true) == true -> listOf(
                        "Überprüfen Sie Ihre Internetverbindung",
                        "Versuchen Sie es später erneut",
                        "Überprüfen Sie die API-Konfiguration"
                    )
                    exception.message?.contains("database", ignoreCase = true) == true ||
                    exception.message?.contains("Datenbank", ignoreCase = true) == true -> listOf(
                        "Löschen Sie die App-Daten und versuchen Sie es erneut",
                        "Installieren Sie die App neu",
                        "Stellen Sie sicher, dass genügend Speicherplatz vorhanden ist"
                    )
                    else -> listOf(
                        "Starten Sie die App neu",
                        "Starten Sie das Gerät neu"
                    )
                }
            }
            else -> listOf(
                "Starten Sie die App neu",
                "Falls das Problem weiterhin besteht, kontaktieren Sie den Support"
            )
        }
    }
    
    /**
     * Create a comprehensive error summary for display in error dialogs
     */
    fun createErrorSummary(
        exception: Throwable,
        context: String = "",
        includeRecovery: Boolean = true
    ): String {
        return buildString {
            appendLine(getUserFriendlyMessage(exception, context))
            appendLine()
            
            appendLine("Technische Details:")
            appendLine("• Fehlertyp: ${exception.javaClass.simpleName}")
            if (exception.cause != null) {
                appendLine("• Grundursache: ${exception.cause?.javaClass?.simpleName}")
            }
            
            if (includeRecovery) {
                appendLine()
                appendLine("Lösungsvorschläge:")
                getRecoverySuggestions(exception).forEach { suggestion ->
                    appendLine("• $suggestion")
                }
            }
        }
    }
}