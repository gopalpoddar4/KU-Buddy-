package `in`.gopalpoddar.kubuddy_app.screen.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import `in`.gopalpoddar.kubuddy_app.data.auth.AuthRepository
import `in`.gopalpoddar.kubuddy_app.data.user.UserRepository

class SettingViewModelFactory(
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository
): ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SettingViewModel(userRepository,authRepository) as T
    }
}