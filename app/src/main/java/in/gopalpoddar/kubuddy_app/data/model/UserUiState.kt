package `in`.gopalpoddar.kubuddy_app.data.model

sealed class UserUiState {
    object Loading: UserUiState()
    object Success: UserUiState()
    object Failed: UserUiState()
}