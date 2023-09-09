import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import util.LocalWindowManager
import util.WindowComponent
import util.WindowConfig
import util.WindowManager
import java.awt.Toolkit
import java.util.*

object MainWindow : WindowComponent {
    context(WindowManager)
    fun show() {
        show(MainWindow, WindowConfig())
    }
    context(BoxScope)
    @Composable
    override fun content() {
        val app = LocalApp.current
        Button(onClick = {
            app.startBreakNow()
        }, Modifier.align(Alignment.Center)) {
            Text("Show example")
        }
    }

}
