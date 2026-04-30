package `in`.gopalpoddar.kubuddy_app.data.auth

data class LoginUiState(
    val email: String="",
    val password:String="",
    val emailError: String?=null,
    val passwordError: String?=null,
    val isLoading: Boolean=false,
    val isSuccess: Boolean=false,
    val errorMessage: String?=null
)
