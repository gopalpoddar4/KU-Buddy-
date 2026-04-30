package `in`.gopalpoddar.kubuddy_app.screen.auth.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import `in`.gopalpoddar.kubuddy_app.data.auth.AuthRepository
import `in`.gopalpoddar.kubuddy_app.data.user.UserRepository

class SignupViewModelFactory(
    private val repo: AuthRepository,
    private val userRepository: UserRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SignupViewModel(repo,userRepository) as T
    }
}