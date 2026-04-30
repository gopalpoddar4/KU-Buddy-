package `in`.gopalpoddar.kubuddy_app.screen.auth.signup

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import `in`.gopalpoddar.kubuddy_app.data.auth.AuthRepository
import `in`.gopalpoddar.kubuddy_app.data.auth.SignUpUiState
import `in`.gopalpoddar.kubuddy_app.data.model.User
import `in`.gopalpoddar.kubuddy_app.data.model.UserUiState
import `in`.gopalpoddar.kubuddy_app.data.user.UserRepository
import kotlinx.coroutines.launch

class SignupViewModel (
    private val repo: AuthRepository,
    private val userRepository: UserRepository
): ViewModel(){

    var userState by mutableStateOf<UserUiState>(UserUiState.Loading)
        private set
    var uiState by mutableStateOf(SignUpUiState())
        private set

    fun onNameChange(name: String){
        uiState = uiState.copy(name = name, nameError = null)
    }

    fun onEmailChange(value: String) {
        uiState = uiState.copy(email = value, emailError = null)
    }

    fun onSemesterChange(value: String){
        uiState = uiState.copy(semester = value, semesterError = null)
    }

    fun onPasswordChange(value: String) {
        uiState = uiState.copy(password = value, passwordError = null)
    }

    fun onConfirmPasswordChange(value: String) {
        uiState = uiState.copy(confirmPassword = value, confirmPasswordError = null)
    }


    private fun validate(): Boolean{
        var isValid = true

        if (uiState.name.isBlank()){
            uiState = uiState.copy(nameError = "Name required")
            isValid = false
        }

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

        if (uiState.password.length < 6){
            uiState = uiState.copy(passwordError = "Must be 6 digit")
            isValid=false
        }

        if (uiState.confirmPassword.isBlank()){
            uiState = uiState.copy(confirmPasswordError = "Enter confirm password")
            isValid=false
        }

        if (uiState.confirmPassword.length < 6){
            uiState = uiState.copy(confirmPasswordError = "Must be 6 digit")
            isValid=false
        }

        if (uiState.password != uiState.confirmPassword){
            uiState = uiState.copy(confirmPasswordError = "Password and confirm password must be same")
            isValid=false
        }

        return isValid
    }

    fun signUp(){

        if (!validate()) return

        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, errorMessage = null)

            val result = repo.signUp(uiState.email, uiState.password)

            uiState = uiState.copy(isLoading = false)


            result.onSuccess {
                uiState = uiState.copy(isSuccess = true)

                val saveUser = userRepository.saveUserData(User(uiState.name,uiState.email,uiState.semester,uiState.password,"Kolhan University"))

                saveUser.onSuccess {
                    userState = UserUiState.Success
                }.onFailure {
                    userState = UserUiState.Failed
                }
               //
            }.onFailure{
                uiState = uiState.copy(
                    errorMessage = it.message ?: "SignUp failed"
                )
            }
        }
    }
}