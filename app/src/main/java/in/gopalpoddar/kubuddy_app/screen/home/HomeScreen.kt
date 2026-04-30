package `in`.gopalpoddar.kubuddy_app.screen.home

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import `in`.gopalpoddar.kubuddy_app.R
import `in`.gopalpoddar.kubuddy_app.data.model.GridItemModel
import `in`.gopalpoddar.kubuddy_app.data.model.User

@Composable
fun HomeScreen(
    goToListScreen:(materialType: String,semester: String)->Unit,
    viewModel: HomeViewModel,
    goToSettings:()-> Unit
) {

    val state = viewModel.userState
    val notice = viewModel.notice.collectAsState()

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)

    ) {

        item {
            HeaderSection(state,goToSettings = goToSettings)
        }

        item {
            Spacer(Modifier.height(16.dp))
            Text(
                "Important Section",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(16.dp,0.dp)
            )
            Spacer(Modifier.height(16.dp))
        }

        item {
            GridSection(state, goToListScreen = goToListScreen)
        }

        item {
            NoticeCard(notice.value)
            Spacer(Modifier.height(20.dp))
        }
    }
}

@Composable
fun HeaderSection(state: User?,goToSettings: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.8f))
            .padding(16.dp, 50.dp, 16.dp, 45.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Welcome",
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = "${state?.name}",
                    color = MaterialTheme.colorScheme.onPrimary,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "Semester ${state?.semester}",
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
                    style = MaterialTheme.typography.bodyMedium
                )
            }
            Image(imageVector = Icons.Default.Settings,
                contentDescription = "Setting Button",
                modifier = Modifier.size(30.dp)
                    .clickable(
                        onClick = {
                            goToSettings()
                        }
                    )
                )
        }


    }
}

@Composable
fun GridSection(state: User?,goToListScreen:(materialType: String,semester: String)->Unit){
    val items = listOf(
        GridItemModel("Notes", R.drawable.notes,"notes"),
        GridItemModel("Syllabus", R.drawable.syllabus,"syllabus"),
        GridItemModel("Set Paper", R.drawable.setpaper,"set_paper"),
        GridItemModel("Video", R.drawable.setpaper,"videos"),
        GridItemModel("Exam News", R.drawable.examnews,"exam_news"),
        GridItemModel("PYQ", R.drawable.pyq,"pyq"),
    )

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .height(220.dp)
            .padding(16.dp, 0.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),

    ) {
        items(items.size){
            GridItem(items[it],state, goToListScreen = goToListScreen)
        }
    }

}

@Composable
fun GridItem(gridItemModel: GridItemModel,state: User?,goToListScreen:(materialType: String,semester: String)->Unit){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
            .padding(10.dp)
            .clickable {
                goToListScreen(gridItemModel.key, "${state?.semester}")
            },
        verticalAlignment = Alignment.CenterVertically

    ) {
        Box(
            modifier = Modifier.size(30.dp)
                .padding(start = 7.dp),
            contentAlignment = Alignment.Center
        ) {
            Image(painter = painterResource(gridItemModel.imageVector), contentDescription = "")
        }
        Spacer(Modifier.width(10.dp))
        Text(gridItemModel.title,
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun NoticeCard(notice: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 0.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f))
            .padding(16.dp, 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Notice",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = notice,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Thank you",
            fontWeight = FontWeight.Bold
        )

        Text(
            text = "KU Buddy",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}