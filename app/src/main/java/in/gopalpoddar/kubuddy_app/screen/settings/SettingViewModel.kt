package `in`.gopalpoddar.kubuddy_app.screen.settings

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import `in`.gopalpoddar.kubuddy_app.data.auth.AccountDeleteUiSate
import `in`.gopalpoddar.kubuddy_app.data.auth.AuthRepository
import `in`.gopalpoddar.kubuddy_app.data.auth.LogoutUiState
import `in`.gopalpoddar.kubuddy_app.data.model.User
import `in`.gopalpoddar.kubuddy_app.data.user.UserRepository
import kotlinx.coroutines.launch

class SettingViewModel (
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
): ViewModel(){

    var userState by mutableStateOf<User?>(null)
        private set

    var logoutState by mutableStateOf(LogoutUiState())
        private set

    var accountDeleteState by mutableStateOf(AccountDeleteUiSate())
        private set

    init {
        loadUser()
    }

    fun loadUser(){
        viewModelScope.launch {
            try {
                val user = userRepository.getUserData()
                userState = user
            }catch (e: Exception){

            }
        }
    }

    fun logout(){
        viewModelScope.launch {
            logoutState = logoutState.copy(Loading = true)
            val result = authRepository.logout()
            logoutState = logoutState.copy(Loading = false)

            result.onSuccess {
                clearLocalUserData()
                logoutState = logoutState.copy(Logout = true)
            }.onFailure {
                logoutState = logoutState.copy(Logout = false)
            }
        }
    }

    fun clearLocalUserData(){
        viewModelScope.launch {
            userRepository.clearLocalData()
        }
    }

    fun deleteUserAccount(password: String, onSuccess:()-> Unit,onError:(String)-> Unit){
        viewModelScope.launch {

            authRepository.reAuthUser(
                password,
                {
                    //Re-Auth Success
                    deleteUserData({
                        //Delete User Data Success
                         deleteUser({
                             //Delete User Success
                             accountDeleteState = accountDeleteState.copy(Success = true)
                             clearLocalUserData()
                             onSuccess()
                         },{
                             //Delete User Failed
                             accountDeleteState = accountDeleteState.copy(Failed = true)
                             onError("Something went wrong! Try again.")
                             restoreUserToDB()
                         })
                    },{
                        //Delete User Data Failed
                        onError("Something went wrong! Try again.")
                    })

                }, {
                    //Re-Auth Failed
                    onError(it)
                })

        }
    }

    fun deleteUser(onSuccess: () -> Unit,failed:()-> Unit){
        viewModelScope.launch {
            authRepository.deleteUser({
                onSuccess()
            },{
                failed()
            })
        }
    }

    fun deleteUserData(onSuccess: () -> Unit,failed:()-> Unit){
        viewModelScope.launch {
            val result = userRepository.deleteUserData()
            result.onSuccess {
                onSuccess()
            }.onFailure {
                failed()
            }
        }
    }

    fun restoreUserToDB(){
        viewModelScope.launch {
            try {
                val user = userRepository.getUserData()
                userRepository.saveUserData(user)
            }catch (e: Exception){

            }

        }
    }

    fun sendFeedbackMail(context: Context){
        val email = "kubuddy.contact@gmail.com"
        val subject = "Feedback for KU Buddy App"
        val body = "My feedback is to.."

        val intent = Intent(Intent.ACTION_SENDTO).apply {
            data = Uri.parse("mailto:")
            putExtra(Intent.EXTRA_EMAIL,arrayOf(email))
            putExtra(Intent.EXTRA_SUBJECT,subject)
            putExtra(Intent.EXTRA_TEXT,body)

        }
        try {
            context.startActivity(intent)
        }catch (e: Exception){
            Toast.makeText(context,e.message, Toast.LENGTH_SHORT).show()
        }
    }
}