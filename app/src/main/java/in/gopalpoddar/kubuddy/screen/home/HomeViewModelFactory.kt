package `in`.gopalpoddar.kubuddy.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import `in`.gopalpoddar.kubuddy.data.auth.AuthRepository
import `in`.gopalpoddar.kubuddy.data.user.UserRepository

class HomeViewModelFactory(
    private val userRepository: UserRepository,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(userRepository) as T
    }
}