package `in`.gopalpoddar.kubuddy.screen.auth.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import `in`.gopalpoddar.kubuddy.navigation.Routes
import `in`.gopalpoddar.kubuddy.screen.auth.AuthTextField
import `in`.gopalpoddar.kubuddy.screen.auth.signup.SignupViewModel

@Composable
fun LoginScreen(
    dontHaveAccount:()-> Unit,
    onLoginSuccess:()-> Unit,
    viewModel: LoginViewModel = viewModel()
){

    val state = viewModel.uiState

    LaunchedEffect(state.isSuccess) {
        if (state.isSuccess){
            onLoginSuccess()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Login", style = MaterialTheme.typography.headlineLarge)

        Spacer(Modifier.height(20.dp))

        OutlinedTextField(
            value = state.email,
            onValueChange = viewModel::onEmailChange,
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            isError = state.emailError !=null,
            singleLine = true,
            supportingText = {
                state.emailError?.let {
                    Text(text = it)
                }
            },
            leadingIcon = {
                IconButton(onClick = {}) {
                    Icon(imageVector = Icons.Filled.Email, contentDescription = "")
                }
            },
            trailingIcon = {
                if (state.email.isNotEmpty()){
                    IconButton(
                        onClick = {
                            viewModel.onEmailChange("")
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            ""
                        )
                    }
                }
            }
        )
        Spacer(Modifier.height(10.dp))
        OutlinedTextField(
            value = state.password,
            onValueChange = viewModel::onPasswordChange,
            label = { Text("Password") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            isError = state.passwordError !=null,
            singleLine = true,
            supportingText = {
                state.passwordError?.let {
                    Text(text = it)
                }
            },
            leadingIcon = {
                IconButton(onClick = {}) {
                    Icon(imageVector = Icons.Filled.Lock, contentDescription = "")
                }
            }
        )

        Spacer(Modifier.height(20.dp))

        Button(
            onClick = {
               viewModel.login()
            },
            enabled = !state.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (state.isLoading){
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp
                )
            }else{
                Text("Login")
            }

        }

        Spacer(Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text("Don't have an account? ", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onBackground)
            Text(
                "Create Account",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable(
                    onClick = { dontHaveAccount() }
                )
            )
        }

        state.errorMessage?.let {
            Spacer(Modifier.height(10.dp))
            Box(modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center){
                Text(it, color = MaterialTheme.colorScheme.error)
            }

        }
    }
}