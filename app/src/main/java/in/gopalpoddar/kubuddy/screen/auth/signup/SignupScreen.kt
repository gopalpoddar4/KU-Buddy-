package `in`.gopalpoddar.kubuddy.screen.auth.signup

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import `in`.gopalpoddar.kubuddy.data.model.UserUiState
import `in`.gopalpoddar.kubuddy.screen.auth.AuthTextField

@Composable
fun SignupScreen(
    onSuccess:()->Unit,
    goToLogin:()-> Unit,
    goToCompleteProfile:()-> Unit,
    viewModel: SignupViewModel
){
    val state = viewModel.uiState
    val userState = viewModel.userState

    LaunchedEffect(userState) {
       when(userState){
           UserUiState.Success -> onSuccess()
           UserUiState.Failed -> goToCompleteProfile()
           else -> Unit
       }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp, 30.dp, 20.dp, 0.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(20.dp))

        Text("Create Account", style = MaterialTheme.typography.headlineLarge, modifier = Modifier.fillMaxWidth())

        Spacer(Modifier.height(20.dp))

        OutlinedTextField(
            value = state.name,
            onValueChange = viewModel::onNameChange,
            label = { Text("Name") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            isError = state.nameError !=null,
            singleLine = true,
            supportingText = {
                state.nameError?.let {
                    Text(text = it)
                }
            },
            leadingIcon = {
                IconButton(onClick = {}) {
                    Icon(imageVector = Icons.Filled.Person, contentDescription = "")
                }
            },
            trailingIcon = {
                if (state.name.isNotEmpty()){
                    IconButton(
                        onClick = {
                            viewModel.onNameChange("")
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

        SemesterDropDown(
            selectedSemester = state.semester,
            onSemesterSelected = viewModel::onSemesterChange
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
        Spacer(Modifier.height(10.dp))

        OutlinedTextField(
            value = state.confirmPassword,
            onValueChange = viewModel::onConfirmPasswordChange,
            label = { Text("Confirm password") },
            shape = RoundedCornerShape(12.dp),
            isError = state.confirmPasswordError !=null,
            singleLine = true,
            supportingText = {
                state.confirmPasswordError?.let {
                    Text(text = it)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                IconButton(onClick = {}) {
                    Icon(imageVector = Icons.Filled.Lock, contentDescription = "")
                }
            }

        )
        Spacer(Modifier.height(20.dp))

        Button(
            onClick = { viewModel.signUp() },
            enabled = !state.isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (state.isLoading){
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    strokeWidth = 2.dp
                )
            }else{
                Text("Create Account")
            }
        }

        Spacer(Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                "Already have an account? ",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                "Login",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.clickable(
                    onClick = { goToLogin() }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SemesterDropDown(
    selectedSemester: String,
    onSemesterSelected: (String) -> Unit
) {

    val semesters = listOf(
        "1",
        "2",
        "3",
        "4",
        "5",
        "6"
    )

    var expanded by remember {
        mutableStateOf(false)
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = {
            expanded = !expanded
        }
    ) {

        OutlinedTextField(
            value = selectedSemester,
            onValueChange = {},
            readOnly = true,
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(),
            label = { Text("Semester") },

            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(
                    expanded = expanded
                )
            },
            leadingIcon = {
                IconButton(onClick = {}) {
                    Icon(imageVector = Icons.Default.AccountBox, contentDescription = "")
                }
            }
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = {
                expanded = false
            }
        ) {
            semesters.forEach { semester ->
                DropdownMenuItem(
                    text = {
                        Text(semester)
                    },
                    onClick = {
                        onSemesterSelected(semester)
                        expanded = false
                    }
                )
            }
        }
    }
}