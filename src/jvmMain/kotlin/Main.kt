import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Tray
import androidx.compose.ui.window.application
import com.sun.jna.platform.win32.Kernel32
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import util.Countdown
import util.WindowManager
import util.os.Os
import util.os.User32
import util.os.User32.LASTINPUTINFO
import java.awt.GraphicsEnvironment
import kotlin.system.exitProcess


class App(private val window: WindowManager) {
    private val interval = 60 * 30
    private val idleTime = 60 * 1000L

    private val countdown = Countdown.start(intervalSeconds = interval) {
        if (!Os.anyAreRunning(listOf("dota2.exe"))) {
            StretchReminder.show(window)
        }
    }

    init {
        GlobalScope.launch {
            while (true) {
                delay(idleTime)
                if (System.currentTimeMillis() - Os.lastActiveTimeMs() > idleTime) {
                    countdown.reset()
                }
            }
        }
    }

    val secondsTillNextReminder get() = countdown.secondsLeft

    fun startBreakNow() {
        countdown.end()
    }
}

val LocalApp = staticCompositionLocalOf<App> { error("No App") }

fun formatSeconds(seconds: Int): String {
    return if (seconds < 60) "${seconds}s" else "${seconds / 60}m ${seconds % 60}s"
}


fun main() {
//    val lii = LASTINPUTINFO()
//    val result = IntByReference()

//    while (true) {
//        val lii = LASTINPUTINFO()
//
//        if (User32.INSTANCE.GetLastInputInfo(lii)) {
//            val lastInputTime = lii.dwTime
//            println("Last Input Time (milliseconds): $lastInputTime")
//        } else {
//            val errorCode: Int = Kernel32.INSTANCE.GetLastError()
//            System.err.println("GetLastInputInfo failed.")
//            System.err.println("Error Code: $errorCode")
//        }
//        Thread.sleep(1000)
//    }


//    if (User32.Instance.GetLastInputInfo(lii.pointer)) {
//        println("Last Input Time: " + lii + " milliseconds")
//    } else {
////        Marshal
//
//    }
    val manager = WindowManager.create(MainWindow, MainWindow.DefaultConfig) {
//    val manager = WindowManager.create(StretchReminder, WindowConfig()) {
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
        MaterialTheme(colors = darkColors) {
            CompositionLocalProvider(LocalApp provides app, LocalContentColor provides MaterialTheme.colors.onBackground) {
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
// 7. Play tuturu on break

//TODO: Second Release
// 0. Require solving a puzzle to stop reminder
// 1. Many auto-generated inspirational quotes
// 2. Config: break interval
// 3. Config: break duration
// 4. Config: break sound
// 5. Config: banned apps
// 6. Decrease break interval in times of high usage
// 7. config: formula for point 6

// Get an array of graphics devices GraphicsDevice [] gs = ; // Loop through each device and print its size for (GraphicsDevice gd : gs) { // Get the display mode of the device DisplayMode dm = gd.getDisplayMode (); // Get the width and height of the device in pixels int width = dm.getWidth (); int height = dm.getHeight (); // Print the size System.out.println ("Monitor size: " + width + " x " + height); }}

