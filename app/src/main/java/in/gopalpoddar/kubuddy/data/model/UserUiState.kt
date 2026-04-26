package `in`.gopalpoddar.kubuddy.data.model

sealed class UserUiState {
    object Loading: UserUiState()
    object Success: UserUiState()
    object Failed: UserUiState()
}