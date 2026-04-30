package `in`.gopalpoddar.kubuddy_app.screen.settings

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import `in`.gopalpoddar.kubuddy_app.R
import `in`.gopalpoddar.kubuddy_app.data.model.User

@Composable
fun SettingScreen(
    viewModel: SettingViewModel,
    onBackPress:()-> Unit,
    onLogout:()-> Unit
){
    val userState = viewModel.userState
    val logoutSate = viewModel.logoutState
    val accountDeleteState = viewModel.accountDeleteState
    val context = LocalContext.current
    var showDeleteDialog by remember { mutableStateOf(false) }


    LaunchedEffect(logoutSate.Logout) {
        if (logoutSate.Logout){
            onLogout()
        }
    }

    LaunchedEffect(accountDeleteState.Success) {
        if (accountDeleteState.Success){
            Toast.makeText(context,"Account Delete", Toast.LENGTH_SHORT).show()
            onLogout()
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 20.dp)
            .background(MaterialTheme.colorScheme.background),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {SettingHeader(onBackPress = onBackPress,{
            Toast.makeText(context,"Feature coming soon", Toast.LENGTH_SHORT).show()
        })  }
        item { ProfileSummary(userState) }
        item {
            QuickActionButton("Feedback", R.drawable.mail) {
                viewModel.sendFeedbackMail(context)
            }
        }
        item { QuickActionButton("Logout", R.drawable.logout) {
            viewModel.logout()
        } }
        item {
            QuickActionButton("Account delete", R.drawable.person) {
                viewModel.deleteUserData()
                showDeleteDialog = true
            }
        }
        item {
            Spacer(Modifier.height(20.dp))
            Text("V 1.0",color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f), fontSize = 12.sp)
        }
    }

    if (showDeleteDialog){
        DeleteAccountSection(viewModel,{
            showDeleteDialog=false
        })
    }

}

@Composable
fun DeleteAccountSection(viewModel: SettingViewModel,onDismis:()-> Unit) {
    val context = LocalContext.current
    var password by remember { mutableStateOf("") }

        AlertDialog(
            onDismissRequest = { onDismis() },
            title = { Text("Confirm Deletion") },
            text = {
                Column {
                    Text("Enter password for confirmation")
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = password,
                        onValueChange = { password = it },
                        placeholder = { Text("Password") },
                        visualTransformation = PasswordVisualTransformation()
                    )
                }
            },
            confirmButton = {
                Button(onClick = {
                    viewModel.deleteUserAccount(password,{
                        viewModel.clearLocalUserData()
                    },{
                        Toast.makeText(context,it, Toast.LENGTH_SHORT).show()
                    })
                }) {
                    Text("Confirm Delete")
                }
            },
            dismissButton = {
                TextButton(onClick = { onDismis()
                    viewModel.restoreUserToDB()

                }) {
                    Text("Cancel")
                }
            },
            properties = DialogProperties(
                dismissOnBackPress = false, // Back button se band nahi hoga
                dismissOnClickOutside = false // Bahar click karne se band nahi hoga
            ),
        )

}

@Composable
fun ProfileSummary(state: User?){
    Column(
        modifier = Modifier.fillMaxWidth()
            .padding(16.dp),
    ) {
        Spacer(Modifier.height(5.dp))
        Text("Profile Summary", fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(3.dp))
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),RoundedCornerShape(7.dp))
                .padding(12.dp)
        ) {

            ProfileSummaryRow("Name",state?.name?:"Username")

            ProfileSummaryRowDivider()

            ProfileSummaryRow("Email",state?.email?:"username@email.in")

            ProfileSummaryRowDivider()

            ProfileSummaryRow("Semester",state?.semester?:"1")

            ProfileSummaryRowDivider()

            ProfileSummaryRow("University",state?.university?:"User University")
        }
    }
}

@Composable
fun SettingHeader(onBackPress: () -> Unit, onEditProfileClick:()-> Unit){
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.8f))
            .padding(16.dp, 60.dp, 16.dp, 35.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(imageVector = Icons.Default.KeyboardArrowLeft,
                contentDescription = "Back Button",
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary),
                modifier = Modifier.size(40.dp)
                    .clickable(
                        onClick = {
                            onBackPress()
                        }
                    )
            )
        }

        Spacer(Modifier.height(10.dp))

        Box(
            modifier = Modifier
                .size(100.dp)
                .background(MaterialTheme.colorScheme.primary,RoundedCornerShape(50))
                .padding(10.dp),

            contentAlignment = Alignment.Center
        ){
            Image(
                painter = painterResource(R.drawable.person),
                contentDescription = "",
                colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary)
            )
        }

        Spacer(Modifier.height(10.dp))
        Box(
            modifier = Modifier
                .border(1.dp, color = MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(7.dp))
                .padding(vertical = 5.dp, horizontal = 10.dp)
                .clickable(
                    onClick = {
                        onEditProfileClick()
                    }
                ),
            contentAlignment = Alignment.Center
        ){
            Text("Edit Profile", color = MaterialTheme.colorScheme.onPrimary, fontSize = 10.sp)
        }
    }
}

@Composable
fun ProfileSummaryRow(label:String,value: String){
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(label, color = MaterialTheme.colorScheme.onBackground,fontWeight = FontWeight.SemiBold,modifier = Modifier.weight(1f))
        Text(value,color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))
    }
}

@Composable
fun ProfileSummaryRowDivider(){
    Spacer(Modifier.height(5.dp))
    Divider(Modifier.fillMaxWidth(), thickness = 1.dp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f))
    Spacer(Modifier.height(5.dp))
}

@Composable
fun QuickActionButton(label: String,icon: Int,onClick:()-> Unit){
    Row(
        modifier = Modifier.fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 5.dp)
            .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f),RoundedCornerShape(7.dp))
            .padding(16.dp)
            .clickable(
                onClick = {
                    onClick()
                }
            )
        ,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(painter = painterResource(icon),
            "",
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onBackground),
            modifier = Modifier.size(20.dp))

        Spacer(Modifier.width(10.dp))
        Text(label,fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onBackground)
    }
}