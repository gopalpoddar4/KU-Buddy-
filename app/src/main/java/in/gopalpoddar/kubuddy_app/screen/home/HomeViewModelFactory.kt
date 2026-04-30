package `in`.gopalpoddar.kubuddy_app.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import `in`.gopalpoddar.kubuddy_app.data.user.UserRepository

class HomeViewModelFactory(
    private val userRepository: UserRepository,
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(userRepository) as T
    }
}