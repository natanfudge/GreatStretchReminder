import androidx.compose.foundation.layout.BoxScope
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
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
    context(BoxScope)
    @Composable
    override fun content() {
        val window = LocalWindowManager.current
        LaunchedEffect(Unit) {
            while(true) {
                //TODO: write an  interatacble timer
            }
        }
        Button(onClick = {
            val screenSize = Toolkit.getDefaultToolkit().screenSize
            window.show(
                StretchReminder, WindowConfig(
                    undecorated = true,
                    transparent = true,
                    state = WindowState(
                        size = DpSize(
                            (getTotalScreenWidth()).dp,
                            // Bugs out without + 1
                            (screenSize.height + 1).dp
                        ),
                        position = WindowPosition.Absolute(0.dp, 0.dp)
                    )
                )
            )
        }, Modifier.align(Alignment.Center)) {
            Text("Show example")
        }
    }

}


fun main() {
    application {
        MaterialTheme(colors = darkColors) {
            WindowManager.display(MainWindow, WindowConfig())
        }
    }
}

val darkColors = darkColors(
    surface = Color(0xFF_40_40_40),
    background = Color(0xFF_20_20_20),
    primary = Color.Cyan
)

fun getTotalScreenWidth(): Int {
    val env = GraphicsEnvironment.getLocalGraphicsEnvironment()
    val devices = env.screenDevices
    return devices.sumOf { it.displayMode.width }
}


//TODO: features:
// First release /////////////
// 1. Display main screen initially with config options
// 2. 'Start break now' in main screen
// 3. Start break every 30 minutes
// 4. Loading bar for break
// 5. Ban reminder when certain apps are open
// 6. Reset time left when away from computer
// 7. Play tuturu on break

//TODO: Second Release
// 1. Many auto-generated inspirational quotes
// 2. Config: break interval
// 3. Config: break duration
// 4. Config: break sound

// Get an array of graphics devices GraphicsDevice [] gs = ; // Loop through each device and print its size for (GraphicsDevice gd : gs) { // Get the display mode of the device DisplayMode dm = gd.getDisplayMode (); // Get the width and height of the device in pixels int width = dm.getWidth (); int height = dm.getHeight (); // Print the size System.out.println ("Monitor size: " + width + " x " + height); }}

