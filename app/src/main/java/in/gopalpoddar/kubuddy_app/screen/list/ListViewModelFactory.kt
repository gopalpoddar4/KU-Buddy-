package `in`.gopalpoddar.kubuddy_app.screen.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ListViewModelFactory(
    private val listRepository: ListRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ListViewModel(listRepository) as T
    }
}