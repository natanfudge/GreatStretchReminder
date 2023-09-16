import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.BitmapPainter
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowState
import okhttp3.OkHttpClient
import util.WindowComponent
import util.WindowConfig
import util.WindowManager
import util.get

object MainWindow : WindowComponent {
    val DefaultConfig = WindowConfig(state = WindowState(height = 400.dp, width = 600.dp))
    context(WindowManager)
    fun show() {
        show(MainWindow, DefaultConfig)
    }

    context(BoxScope)
    @Composable
    override fun content() {
        val app = LocalApp.current
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.align(Alignment.Center)) {
            OutlinedButton(onClick = { app.startBreakNow() }, Modifier.padding(10.dp)) {
                Text("Show example")
            }
            Text("Next break in: ${formatSeconds(app.secondsTillNextReminder)}",
                Modifier.padding(10.dp),
                color = MaterialTheme.colors.onBackground.copy(alpha = 0.7f)
            )
        }

    }

}


