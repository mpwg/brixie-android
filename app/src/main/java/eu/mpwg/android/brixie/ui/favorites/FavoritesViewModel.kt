package eu.mpwg.android.brixie.ui.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.mpwg.android.brixie.data.local.entities.SetEntity
import eu.mpwg.android.brixie.data.repository.SetRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class FavoritesUiState(
    val favoriteSets: List<SetEntity> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class FavoritesViewModel(
    private val setRepository: SetRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()
    
    init {
        loadFavorites()
    }
    
    private fun loadFavorites() {
        viewModelScope.launch {
            setRepository.getFavoriteSets()
                .catch { throwable ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = throwable.message
                    )
                }
                .collect { favorites ->
                    _uiState.value = _uiState.value.copy(
                        favoriteSets = favorites,
                        isLoading = false,
                        errorMessage = null
                    )
                }
        }
    }
    
    fun toggleFavorite(setNum: String) {
        viewModelScope.launch {
            setRepository.toggleFavorite(setNum)
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}