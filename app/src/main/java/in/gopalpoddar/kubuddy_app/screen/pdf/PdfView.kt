package `in`.gopalpoddar.kubuddy_app.screen.pdf

import android.content.Context
import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.ParcelFileDescriptor
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream


@Composable
fun PdfView(
    file: File
){
    val context = LocalContext.current

    var renderer by remember { mutableStateOf<PdfRenderer?>(null) }
    var descriptor by remember { mutableStateOf<ParcelFileDescriptor?>(null) }
    var pageCount by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
       // val file = copyPdfFromAssets(context,file)
        val (r,d) = openPdfRenderer(file)
        renderer = r
        descriptor = d
        pageCount = r.pageCount
    }

    DisposableEffect(Unit) {
        onDispose {
            renderer?.close()
            descriptor?.close()
        }
    }

    if (renderer == null){
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
            CircularProgressIndicator()
        }
    }else{
        val pagerState = rememberPagerState(pageCount = {pageCount})
        val scope = rememberCoroutineScope()
        Column(
            Modifier.fillMaxSize()
                .padding(bottom = 80.dp)
        ) {
            Box(
                modifier = Modifier.weight(1f)
            ) { HorizontalPager(
                state = pagerState,
            ) { page->
                var bitmap by remember { mutableStateOf<Bitmap?>(null) }

                LaunchedEffect(page) {
                    try {
                        bitmap = renderPage(renderer!!,page)
                    }catch (e: Exception){
                        Log.e("PDF","Render error: ${e.message}")
                    }
                }
                bitmap?.let { bmp->
                    var scale by remember { mutableStateOf(1f) }
                    var offsetX by remember { mutableStateOf(0f) }
                    var offsetY by remember { mutableStateOf(0f) }

                    Box(
                        modifier = Modifier
                            .pointerInput(Unit){
                                detectTransformGestures { _,pan, zoom, rotation ->
                                    val newScale = (scale*zoom).coerceIn(1f,5f)

                                    if (newScale>1f){
                                        offsetX += pan.x
                                        offsetY += pan.y
                                    }
                                    scale = newScale
                                    if (scale <= 1f){
                                        scale = 1f
                                        offsetX = 0f
                                        offsetY =0f
                                    }
                                }
                            }
                    ){
                        Image(
                            bitmap = bmp.asImageBitmap(),
                            contentDescription = "Page $page",
                            modifier = Modifier
                                .fillMaxWidth()
                                .graphicsLayer{
                                    scaleX=scale
                                    scaleY=scale
                                    translationX=offsetX
                                    translationY=offsetY
                                }
                        )

                    }
                }
            }}


            Spacer(Modifier.height(20.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween

            ) {
                Button(
                    onClick = {
                        val prev = (pagerState.currentPage - 1)
                            .coerceAtLeast(0)

                        scope.launch {
                            pagerState.animateScrollToPage(prev)
                        }
                    }
                ) {
                    Text("< Prev")
                }
                Text("Page ${pagerState.currentPage + 1} / $pageCount")
                Button(
                    onClick = {
                        val next = (pagerState.currentPage + 1)
                            .coerceAtMost(pageCount - 1)

                         scope.launch {
                             pagerState.animateScrollToPage(next)
                         }
                    }
                ) {
                    Text("Next >")
                }
            }
        }
    }

}

fun openPdfRenderer(file: File): Pair<PdfRenderer, ParcelFileDescriptor>{
    val descriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
    val renderer = PdfRenderer(descriptor)
    return Pair(renderer,descriptor)
}

fun renderPage(renderer: PdfRenderer,pageIndex: Int): Bitmap{
    val page = renderer.openPage(pageIndex)

    val bitmap = Bitmap.createBitmap(
        page.width*2,
        page.height*2,
        Bitmap.Config.ARGB_8888
    )

    page.render(bitmap,null,null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
    page.close()

    return bitmap
}

fun copyPdfFromAssets(context: Context,fileName: String): File{
    val file = File(context.cacheDir,fileName)

    if (!file.exists()){
        context.assets.open(fileName).use { input ->
            FileOutputStream(file).use { outputStream ->
                input.copyTo(outputStream)
            }
        }
    }

    return file
}


