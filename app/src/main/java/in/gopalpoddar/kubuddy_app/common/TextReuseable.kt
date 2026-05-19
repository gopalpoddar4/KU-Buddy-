package `in`.gopalpoddar.kubuddy_app.common

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun HeaderText(text: String){
    Text(
        text = text,
        color = MaterialTheme.colorScheme.onPrimary,
        fontSize = 14.sp,
        fontWeight = FontWeight.Bold

    )
}