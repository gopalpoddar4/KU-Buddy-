package `in`.gopalpoddar.kubuddy_app.screen.home

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import `in`.gopalpoddar.kubuddy_app.data.model.User
import `in`.gopalpoddar.kubuddy_app.data.user.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HomeViewModel (
    private val userRepository: UserRepository,
): ViewModel(){

    private val _notice = MutableStateFlow("")
    val notice = _notice.asStateFlow()
    var userState by mutableStateOf<User?>(null)
        private set

    init {
        loadUser()
        getNotice()
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

    fun getNotice(){
        viewModelScope.launch {
            try {
                 val notice = userRepository.getNotice()
                Log.d("NOTICE", notice)
                _notice.value = notice
            }catch (e: Exception){

            }
        }
    }




}