package eu.mpwg.android.brixie.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import eu.mpwg.android.brixie.data.repository.ThemeRepository
import eu.mpwg.android.brixie.data.repository.SetRepository
import eu.mpwg.android.brixie.ui.themes.ThemesViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(
    private val themeRepository: ThemeRepository,
    private val setRepository: SetRepository
) : ViewModelProvider.Factory {
    
    override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {
        return when (modelClass) {
            ThemesViewModel::class.java -> ThemesViewModel(themeRepository) as T
            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }
}