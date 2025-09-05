package eu.mpwg.android.brixie.ui.setdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.mpwg.android.brixie.data.local.entities.SetEntity
import eu.mpwg.android.brixie.data.repository.SetRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class SetDetailUiState(
    val set: SetEntity? = null,
    val isLoading: Boolean = false,
    val errorMessage: String? = null
)

class SetDetailViewModel(
    private val setRepository: SetRepository,
    private val setNum: String
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(SetDetailUiState())
    val uiState: StateFlow<SetDetailUiState> = _uiState.asStateFlow()
    
    init {
        loadSetDetails()
    }
    
    private fun loadSetDetails() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            
            // First try to get from local cache
            val cachedSet = setRepository.getSet(setNum)
            if (cachedSet != null) {
                _uiState.value = _uiState.value.copy(
                    set = cachedSet,
                    isLoading = false
                )
            }
            
            // Then refresh from API
            setRepository.refreshSetDetails(setNum)
                .onSuccess { refreshedSet ->
                    _uiState.value = _uiState.value.copy(
                        set = refreshedSet ?: cachedSet,
                        isLoading = false,
                        errorMessage = null
                    )
                }
                .onFailure { throwable ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = if (cachedSet == null) throwable.message else null
                    )
                }
        }
    }
    
    fun toggleFavorite() {
        viewModelScope.launch {
            _uiState.value.set?.let { set ->
                setRepository.toggleFavorite(set.setNum)
                // Update local state immediately for better UX
                _uiState.value = _uiState.value.copy(
                    set = set.copy(isFavorite = !set.isFavorite)
                )
            }
        }
    }
    
    fun retry() {
        loadSetDetails()
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}