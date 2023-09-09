import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.darkColors
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Tray
import androidx.compose.ui.window.application
import util.Countdown
import util.WindowConfig
import util.WindowManager
import java.awt.GraphicsEnvironment
import kotlin.system.exitProcess

//class WindowController : WindowState by WindowState() {
//    var alwaysOnTop by mutableStateOf(false)
//    var show = true
//
////    var decorated by mutableStateOf(true)
////    var transparent by mutableStateOf(false)
//
//}


// see https://stackoverflow.com/questions/71993178/unable-to-bring-window-to-foreground-with-compose-desktop

class App(private val window: WindowManager) {
    private val countdown = Countdown.start(intervalSeconds = 60 * 30) {
        StretchReminder.show(window)
    }

    val secondsTillNextReminder get() = countdown.secondsLeft

    fun startBreakNow() {
        countdown.end()
    }
}

val LocalApp = staticCompositionLocalOf<App> { error("No App") }

private fun formatSeconds(seconds: Int): String {
    return if (seconds < 60) "${seconds}s" else "${seconds / 60}m ${seconds % 60}s"
}


fun main() {
    val manager = WindowManager.create(MainWindow, WindowConfig()) {
        val app = LocalApp.current
        Tray(
            icon = painterResource("icon.png"),
            menu = {
                Item("Start break now", onClick = { app.startBreakNow() })
                Item(
                    "Exit",
                    onClick = { exitProcess(1) }
                )
            },
            onAction = {
                MainWindow.show()
            },
            tooltip = "Next break in: ${formatSeconds(app.secondsTillNextReminder)}"
        )
    }
    val app = App(manager)
    application {
        CompositionLocalProvider(LocalApp provides app) {
            MaterialTheme(colors = darkColors) {
                WindowManager.display(manager)
            }
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
// 4. Loading bar for break
// 5. Ban reminder when certain apps are open
// 6. Reset time left when away from computer
// 7. Play tuturu on break

//TODO: Second Release
// 0. Require solving a puzzle to stop reminder
// 1. Many auto-generated inspirational quotes
// 2. Config: break interval
// 3. Config: break duration
// 4. Config: break sound

// Get an array of graphics devices GraphicsDevice [] gs = ; // Loop through each device and print its size for (GraphicsDevice gd : gs) { // Get the display mode of the device DisplayMode dm = gd.getDisplayMode (); // Get the width and height of the device in pixels int width = dm.getWidth (); int height = dm.getHeight (); // Print the size System.out.println ("Monitor size: " + width + " x " + height); }}

