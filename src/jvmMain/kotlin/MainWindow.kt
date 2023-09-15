import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowState
import org.succlz123.lib.imageloader.ImageAsyncImageUrl
import org.succlz123.lib.imageloader.ImageAsyncVectorUrl
import org.succlz123.lib.imageloader.core.ImageCallback
import util.WindowComponent
import util.WindowConfig
import util.WindowManager

object MainWindow : WindowComponent {
    val DefaultConfig = WindowConfig(state = WindowState(height = 400.dp, width = 600.dp))
    context(WindowManager)
    fun show() {
        show(MainWindow,DefaultConfig)
    }

    context(BoxScope)
    @Composable
    override fun content() {
        val app = LocalApp.current
        ImageAsyncImageUrl("https://zenquotes.io/api/image",
//            density = LocalDensity.current,
            imageCallback = ImageCallback {
                Image(modifier = Modifier.size(640.dp), painter = it, contentDescription = "vector")
            })
//        ImageAsyncVectorUrl("https://zenquotes.io/api/image",
//            density = LocalDensity.current,
//            imageCallback = ImageCallback {
//                Image(modifier = Modifier.size(64.dp), painter = it, contentDescription = "vector")
//            })
//        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.align(Alignment.Center)) {
//            OutlinedButton(onClick = { app.startBreakNow() }, Modifier.padding(10.dp)) {
//                Text("Show example")
//            }
//            Text("Next break in: ${formatSeconds(app.secondsTillNextReminder)}",
//                Modifier.padding(10.dp),
//                color = MaterialTheme.colors.onBackground.copy(alpha = 0.7f)
//            )
//        }

    }

}
