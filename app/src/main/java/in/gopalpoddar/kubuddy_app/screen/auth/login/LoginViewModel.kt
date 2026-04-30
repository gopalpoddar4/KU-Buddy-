package `in`.gopalpoddar.kubuddy_app.screen.auth.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import `in`.gopalpoddar.kubuddy_app.data.auth.AuthRepository
import `in`.gopalpoddar.kubuddy_app.data.auth.LoginUiState
import kotlinx.coroutines.launch

class LoginViewModel(private val repository: AuthRepository = AuthRepository() ) : ViewModel(){

    var uiState by mutableStateOf(LoginUiState())
        private set

    fun onEmailChange(email: String){
        uiState = uiState.copy(email = email, emailError = null)
    }

    fun onPasswordChange(password: String){
        uiState = uiState.copy(password = password, passwordError = null)
    }

    private fun validate(): Boolean{
        var isValid = true

        if (uiState.email.isBlank()){
            uiState = uiState.copy(emailError = "Email required")
            isValid = false
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(uiState.email).matches()){
            uiState = uiState.copy(emailError = "Invalid email")
            isValid = false
        }

        if (uiState.password.isBlank()){
            uiState = uiState.copy(passwordError = "Enter password")
            isValid=false
        }

        return isValid
    }


    fun login(){

        if (!validate()) return

        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, errorMessage = null)
            val result = repository.login(uiState.email, uiState.password)
            uiState = uiState.copy(isLoading = false)

            result.onSuccess {
                uiState = uiState.copy(isSuccess = true)
            }.onFailure {
                uiState = uiState.copy(
                    errorMessage = it.message?:"Login failed"
                )
            }
        }
    }
}