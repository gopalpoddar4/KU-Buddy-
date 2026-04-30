package `in`.gopalpoddar.kubuddy_app.screen.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import `in`.gopalpoddar.kubuddy_app.data.model.ListUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ListViewModel (
    private val listRepository: ListRepository
): ViewModel(){

   private val _listUiState = MutableStateFlow(ListUiState())
    val listUiState: StateFlow<ListUiState> = _listUiState

    init {
        getStudyMaterial()
    }

    fun getStudyMaterial(){
        _listUiState.value = _listUiState.value.copy(isLoading = true)
        viewModelScope.launch {
            try {
                val data = listRepository.getStudyMaterial()
                _listUiState.value = ListUiState(
                    isLoading = false,
                    data = data
                )
            }catch (e: Exception){
                _listUiState.value = ListUiState(
                    isLoading = false,
                    error = e.message
                )
            }
        }
    }
}