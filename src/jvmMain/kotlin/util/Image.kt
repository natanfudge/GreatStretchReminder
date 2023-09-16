package util

import androidx.compose.foundation.Image
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.loadImageBitmap
import okhttp3.OkHttpClient


private val client = OkHttpClient()
@Composable
fun NetworkImage(url: String, modifier: Modifier = Modifier) {
    val image by produceState<BitmapPainter?>(null) {
        value = downloadImagePainter(url)
    }
    if (image == null) {
        CircularProgressIndicator(modifier)
    } else {
        Image(image!!, "Image", modifier)
    }
}


suspend fun downloadImagePainter(url: String): BitmapPainter {
    val response = client.get(url)
    val bitmap = loadImageBitmap(response.body.byteStream())
    return BitmapPainter(bitmap)
}