package `in`.gopalpoddar.kubuddy.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import `in`.gopalpoddar.kubuddy.data.auth.AuthRepository
import `in`.gopalpoddar.kubuddy.data.user.UserRepository

class SettingViewModelFactory(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
): ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingViewModel(userRepository,authRepository) as T
    }
}