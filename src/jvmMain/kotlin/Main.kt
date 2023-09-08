import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import util.LocalWindowManager
import util.WindowComponent
import util.WindowConfig
import util.WindowManager
import java.awt.GraphicsEnvironment
import java.awt.Toolkit

//class WindowController : WindowState by WindowState() {
//    var alwaysOnTop by mutableStateOf(false)
//    var show = true
//
////    var decorated by mutableStateOf(true)
////    var transparent by mutableStateOf(false)
//
//}


// see https://stackoverflow.com/questions/71993178/unable-to-bring-window-to-foreground-with-compose-desktop

object MainWindow : WindowComponent {
    @Composable
    override fun content() {
        var isDialogOpen by remember { mutableStateOf(false) }
        val focusRequester = remember { FocusRequester() }
        if (isDialogOpen) {
            Dialog(onCloseRequest = { isDialogOpen = false }) {
                Text("Halo")
            }
        }
        val window = LocalWindowManager.current

        Box(Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.5f))) {
            Button(onClick = {
                GlobalScope.launch {
                    window.hide(MainWindow)
//                    window.show(St)
//                    delay(3000)
//                window.alwaysOnTop = true
//                window.size = DpSize(3000.dp, 1080.dp)
//                window.show = false
//            window.decorated = false
//            window.transparent = true
                }

            }, Modifier.focusRequester(focusRequester)) {
                Text("Open")
            }
        }
    }

}


fun main() = application {
    val screenSize = Toolkit.getDefaultToolkit().screenSize
    MaterialTheme {
        WindowManager.display(
            StretchReminder, WindowConfig(
                undecorated = true, transparent = true,  state = WindowState(
                    size = DpSize((screenSize.width + 1).dp , (screenSize.height + 1).dp), position = WindowPosition.Absolute(0.dp,0.dp)
                )
            )
        )
    }

}

//fun getTotalScreenWidth() {
//    val env = GraphicsEnvironment.getLocalGraphicsEnvironment()
//    val devices = env.screenDevices
//    val totalWidth = devices.sumOf { it.displayMode.width }
//}

// Get an array of graphics devices GraphicsDevice [] gs = ; // Loop through each device and print its size for (GraphicsDevice gd : gs) { // Get the display mode of the device DisplayMode dm = gd.getDisplayMode (); // Get the width and height of the device in pixels int width = dm.getWidth (); int height = dm.getHeight (); // Print the size System.out.println ("Monitor size: " + width + " x " + height); }}

