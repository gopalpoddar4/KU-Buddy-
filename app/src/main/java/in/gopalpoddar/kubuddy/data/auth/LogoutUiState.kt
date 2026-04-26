package `in`.gopalpoddar.kubuddy.data.auth

data class LogoutUiState(
    val Loading: Boolean=false,
    val Logout: Boolean=false,
)

data class AccountDeleteUiSate(
    val Loading: Boolean=false,
    val Success: Boolean=false,
    val Failed: Boolean=false
)
