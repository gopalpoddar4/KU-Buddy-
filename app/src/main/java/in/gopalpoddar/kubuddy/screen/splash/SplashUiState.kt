package `in`.gopalpoddar.kubuddy.screen.splash

sealed class SplashUiState {
    object Loading: SplashUiState()
    object LoggedIn: SplashUiState()
    object NotLoggedIn: SplashUiState()
}