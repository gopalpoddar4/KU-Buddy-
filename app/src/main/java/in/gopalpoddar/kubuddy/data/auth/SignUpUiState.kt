package `in`.gopalpoddar.kubuddy.data.auth

data class SignUpUiState(
    val name: String = "",
    val email: String = "",
    val semester: String="",
    val password: String="",
    val confirmPassword: String="",
    val nameError: String?=null,
    val emailError: String?=null,
    val semesterError: String?=null,
    val passwordError: String?=null,
    val confirmPasswordError: String?=null,
    val isLoading: Boolean = false,
    val isSuccess: Boolean= false,
    val completeAccountCreated: Boolean=false,
    val errorMessage: String?= null

)