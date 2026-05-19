package `in`.gopalpoddar.kubuddy_app.screen.pdf

import android.app.Activity
import android.content.Context
import android.util.Log
import android.view.WindowManager
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import `in`.gopalpoddar.kubuddy_app.common.HeaderText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.URL

@Composable
fun PDFScreen(
    pdfKey: String,
    pdfName: String
){
    val context = LocalContext.current

    val activity = LocalContext.current as Activity

    DisposableEffect(Unit) {
        activity.window.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        onDispose {
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_SECURE)
        }
    }

    var file by remember { mutableStateOf<File?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    val cleanKey = pdfKey.removeSurrounding("{","}")
    val cleanName = pdfName.removeSurrounding("{","}")

    Log.d("URL",cleanName)

    Log.d("URL",cleanKey)


    LaunchedEffect(cleanKey) {
        try {
            file = downloadPdfIfNeeded(context,cleanKey)
        }catch (e: Exception){
            e.printStackTrace()
            Log.d("PDF_DEBUG","Catch Block")
        }
        isLoading=false
    }

    if (isLoading){
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else{
        file?.let {
            Column(
                modifier = Modifier.fillMaxSize()
            ){
                PDFHeaderSection(cleanName)
                PdfView(it)
            }

        }
    }
}

suspend fun downloadPdfIfNeeded(
    context: Context,
    url: String
): File= withContext(Dispatchers.IO){

    val folder = File(context.filesDir,"pdfs")
    if (!folder.exists()) folder.mkdir()

    val fileName = url.hashCode().toString()
    val file = File(folder, "$fileName.pdf")

    if (file.exists() && file.length() > 50*1024) {
        Log.d("FILE","File exist and return")
        return@withContext file
    }
    else{
        Log.d("FILE","File didn't exist")
        file.delete()
    }


    val connection = URL(url).openConnection()
    connection.connectTimeout = 15000
    connection.readTimeout = 15000

    connection.setRequestProperty("Accept", "application/pdf")

    val input = connection.getInputStream()

    val tempFile = File(folder, "$fileName.temp")
    val output = FileOutputStream(tempFile)

    input.copyTo(output)

    output.flush()
    output.close()
    input.close()

    if (file.exists()) file.delete()


    val success = tempFile.renameTo(file)

    if (!success){
        throw Exception("File rename failed")
    }

    if (file.length() < 50*1024){
        throw Exception("Download file is too small (likely not PDF)")
    }

    // 🔥 DEBUG (बहुत जरूरी)
    val firstLine = file.inputStream().bufferedReader().use { it.readLine() }
    android.util.Log.d("PDF_DEBUG", "First line: $firstLine")
    android.util.Log.d("PDF_DEBUG", "File size: ${file.length()}")

    // ❗ validate PDF
    if (!firstLine.startsWith("%PDF")) {
        throw Exception("Downloaded file is not a valid PDF")
    }


    return@withContext file
}

@Composable
fun PDFHeaderSection(pdfName: String){
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.8f))
            .padding(16.dp, 60.dp, 16.dp, 40.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            HeaderText(pdfName)
        }

    }
}