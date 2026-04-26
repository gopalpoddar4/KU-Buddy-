package `in`.gopalpoddar.kubuddy.screen.splash

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SplashViewModel : ViewModel(){
     private val auth = FirebaseAuth.getInstance()

    var state by mutableStateOf<SplashUiState>(SplashUiState.Loading)
        private set

    init {
        checkUser()
    }

    private fun checkUser(){
        viewModelScope.launch {
            delay(1500)

            val user = auth.currentUser

            state = if (user != null){
                SplashUiState.LoggedIn
            }else{
                SplashUiState.NotLoggedIn
            }
        }
    }
}