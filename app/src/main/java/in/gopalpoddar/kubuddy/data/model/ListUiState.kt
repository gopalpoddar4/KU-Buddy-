package `in`.gopalpoddar.kubuddy.data.model

data class ListUiState(
    val isLoading: Boolean=false,
    val data: List<ParentModel> = emptyList(),
    val error: String? = null
)
