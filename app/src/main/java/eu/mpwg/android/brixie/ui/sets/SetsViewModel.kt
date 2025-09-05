package eu.mpwg.android.brixie.ui.sets

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import eu.mpwg.android.brixie.data.local.entities.SetEntity
import eu.mpwg.android.brixie.data.repository.SetRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

data class SetsUiState(
    val sets: List<SetEntity> = emptyList(),
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val searchQuery: String = "",
    val selectedThemeId: Int? = null
)

class SetsViewModel(
    private val setRepository: SetRepository
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(SetsUiState())
    val uiState: StateFlow<SetsUiState> = _uiState.asStateFlow()
    
    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing.asStateFlow()
    
    init {
        loadSets()
    }
    
    fun setThemeFilter(themeId: Int?) {
        _uiState.value = _uiState.value.copy(selectedThemeId = themeId)
        loadSets()
    }
    
    fun setSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        if (query.isBlank()) {
            loadSets()
        } else {
            searchSets(query)
        }
    }
    
    private fun loadSets() {
        viewModelScope.launch {
            val currentState = _uiState.value
            val flow = when {
                currentState.selectedThemeId != null -> {
                    setRepository.getSetsByTheme(currentState.selectedThemeId)
                }
                else -> setRepository.getAllSets()
            }
            
            flow.catch { throwable ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = throwable.message
                    )
                }
                .collect { sets ->
                    _uiState.value = _uiState.value.copy(
                        sets = sets,
                        isLoading = false,
                        errorMessage = null
                    )
                }
        }
    }
    
    private fun searchSets(query: String) {
        viewModelScope.launch {
            setRepository.searchSets(query)
                .catch { throwable ->
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        errorMessage = throwable.message
                    )
                }
                .collect { sets ->
                    _uiState.value = _uiState.value.copy(
                        sets = sets,
                        isLoading = false,
                        errorMessage = null
                    )
                }
        }
    }
    
    fun refreshSets() {
        viewModelScope.launch {
            _isRefreshing.value = true
            _uiState.value = _uiState.value.copy(isLoading = true, errorMessage = null)
            
            val currentState = _uiState.value
            setRepository.refreshSets(
                pageSize = 100,
                themeId = currentState.selectedThemeId
            )
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
    
    fun toggleFavorite(setNum: String) {
        viewModelScope.launch {
            setRepository.toggleFavorite(setNum)
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(errorMessage = null)
    }
}