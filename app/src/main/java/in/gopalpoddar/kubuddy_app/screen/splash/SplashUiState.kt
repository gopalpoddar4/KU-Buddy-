package `in`.gopalpoddar.kubuddy_app.screen.splash

sealed class SplashUiState {
    object Loading: SplashUiState()
    object LoggedIn: SplashUiState()
    object NotLoggedIn: SplashUiState()
}