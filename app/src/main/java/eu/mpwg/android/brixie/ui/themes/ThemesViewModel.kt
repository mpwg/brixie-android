package eu.mpwg.android.brixie.ui.themes

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.mpwg.android.brixie.data.local.entities.ThemeEntity
import eu.mpwg.android.brixie.data.repository.ThemeRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class ThemesUiState(
    val themes: List<ThemeEntity> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class ThemesViewModel(
    private val themeRepository: ThemeRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(ThemesUiState())
    val uiState: StateFlow<ThemesUiState> = _uiState.asStateFlow()
    
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()
    
    init {
        loadThemes()
    }
    
    private fun loadThemes() {
        viewModelScope.launch {
            themeRepository.getRootThemes()
                .catch { throwable ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = throwable.message
                    )
                }
                .collect { themes ->
                    _uiState.value = _uiState.value.copy(
                        themes = themes,
                        isLoading = false,
                        errorMessage = null
                    )
                }
        }
    }
    
    fun refreshThemes() {
        viewModelScope.launch {
            _isRefreshing.value = true
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            
            themeRepository.refreshThemes(pageSize = 100)
                .onSuccess {
                    // Data will be automatically updated through the Flow
                }
                .onFailure { throwable ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = throwable.message
                    )
                }
            
            _isRefreshing.value = false
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}