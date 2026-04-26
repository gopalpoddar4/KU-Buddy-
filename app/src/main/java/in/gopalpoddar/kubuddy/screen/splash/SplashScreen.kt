package `in`.gopalpoddar.kubuddy.screen.splash

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import `in`.gopalpoddar.kubuddy.navigation.Routes
import `in`.gopalpoddar.kubuddy.ui.theme.Typography

@Composable
fun SplashScreen(
    goToHome:()-> Unit,
    goToLogin:()-> Unit,
    viewModel: SplashViewModel = viewModel()
) {

    val state = viewModel.state

    LaunchedEffect(state) {
        when(state){
            SplashUiState.LoggedIn -> goToHome()
            SplashUiState.NotLoggedIn -> goToLogin()
            else -> Unit
        }
    }


    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(
            "KU Buddy",
            style = Typography.headlineLarge
        )
    }
}