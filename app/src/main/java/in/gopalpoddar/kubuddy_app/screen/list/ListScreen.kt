package `in`.gopalpoddar.kubuddy_app.screen.list

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import java.net.URLEncoder

@Composable
fun ListScreen(
    materialType: String?,
    semester: String?,
    viewModel: ListViewModel,
    goToPdfScreen:(String, String)-> Unit,
    onBackPress: () -> Unit
){
    var expendedIndex by remember { mutableStateOf(-1) }
    //val context = LocalContext.current
    Log.d("KUBUDDY","Material Type: $materialType, Semester: $semester")

    val listUiState by viewModel.listUiState.collectAsState()

    when{
        listUiState.isLoading->{
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
            ) {
                ListHeaderSection(semester,materialType, onBackPress = onBackPress)
                Box(
                    modifier = Modifier.fillMaxSize(1f),
                    contentAlignment = Alignment.Center
                ){
                    CircularProgressIndicator()
                }
            }
        }
        listUiState.error != null ->{
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
            ) {
                ListHeaderSection(semester,materialType, onBackPress = onBackPress)
                Box(
                    modifier = Modifier.fillMaxSize(1f),
                    contentAlignment = Alignment.Center
                ){
                    Text("Something went wrong, Please try again.")
                }
            }
        }

        else->{
            Log.d("KUBUDDY","${listUiState.data.size}")

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                ListHeaderSection(semester,materialType, onBackPress = onBackPress)
                Spacer(Modifier.height(10.dp))
                LazyColumn(
                    modifier = Modifier
                        .padding(16.dp, 0.dp)
                ) {

                    itemsIndexed(listUiState.data){ index,item->
                        Column(
                            modifier = Modifier
                                .padding(bottom = 12.dp)
                                .animateContentSize()
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),RoundedCornerShape(7.dp))
                                .clickable{
                                    expendedIndex = if (expendedIndex == index) -1 else index
                                }
                                .padding(16.dp)
                        ) {
                            item.name?.let {
                                Text(
                                    text = it,
                                    color = MaterialTheme.colorScheme.onSurface,
                                    style = MaterialTheme.typography.titleSmall
                                )
                            }

                            if (expendedIndex == index){
                                Spacer(Modifier.height(16.dp))
                                item.child?.forEach {
                                    val url = URLEncoder.encode(it.pdfKey,"UTF-8")
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .background(MaterialTheme.colorScheme.background,RoundedCornerShape(7.dp))
                                            .clickable(
                                                onClick = {
                                                    goToPdfScreen(url, it.name)
                                                }
                                            )
                                            .padding(16.dp)
                                    ) {
                                        Text(
                                            text = it.name,
                                            color = MaterialTheme.colorScheme.onBackground,
                                            style = MaterialTheme.typography.bodyMedium
                                        )
                                    }
                                    Spacer(Modifier.height(7.dp))
                                }
                            }
                        }
                    }
                }

            }

        }
    }
}

@Composable
fun ListHeaderSection(semester: String?, materialType: String?, onBackPress: () -> Unit){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.8f))
            .padding(16.dp, 60.dp, 16.dp, 40.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Image(imageVector = Icons.Default.KeyboardArrowLeft,
            contentDescription = "Back Button",
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.onPrimary),
            modifier = Modifier.size(40.dp)
                .clickable(
                    onClick = {
                        onBackPress()
                    }
                ),

        )
        Row(
            modifier = Modifier.weight(1f)
                .padding(end = 40.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Semester $semester $materialType",
                color = MaterialTheme.colorScheme.onPrimary,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold

            )
        }

    }
}