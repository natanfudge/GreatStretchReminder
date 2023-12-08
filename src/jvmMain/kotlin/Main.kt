import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.Tray
import androidx.compose.ui.window.application
import util.Countdown
import util.WindowManager
import util.downloadImagePainter
import util.every
import util.os.Os
import java.awt.GraphicsEnvironment
import kotlin.math.roundToInt
import kotlin.math.sqrt
import kotlin.system.exitProcess


class App(private val window: WindowManager) {
    private val breakIntervalSeconds = 60 * 35
    private val idleTime = 60 * 1000L

    private val countdown: Countdown = Countdown.start(intervalSeconds = breakIntervalSeconds) {
        if (!Os.anyAreRunning(listOf("dota2.exe", "dontstarve_steam_x64.exe"))) {
            StretchReminder.show(window, downloadImagePainter("https://zenquotes.io/api/image"))
            consecutiveBreaksWithoutIdle++
            updateCountdownInterval()
        }
    }

    // If the stretch reminder keeps on appearing without the user being idle,
    // it means he is using the computer consecutively for a long time
    private var consecutiveBreaksWithoutIdle = 0

    private fun updateCountdownInterval() {
        // During periods of high use, reduce the interval
        // See break-interval.png
        val modifier = (1.0 / 2) + (1 / (2 * sqrt(sqrt(1.0 + consecutiveBreaksWithoutIdle))))
        countdown.intervalSeconds = (breakIntervalSeconds * modifier).roundToInt()
    }
    init {
        every(idleTime) {
            if (isIdle()) {
                println("Resetting")
                consecutiveBreaksWithoutIdle = 0
                updateCountdownInterval()
                countdown.reset()
            }
        }
    }

    private fun isIdle(): Boolean {
        println("Checking idle. Current time: ${System.currentTimeMillis()}, os last active time: ${Os.lastActiveTimeMs()}, diff: ${System.currentTimeMillis() - Os.lastActiveTimeMs()}")
        return System.currentTimeMillis() - Os.lastActiveTimeMs() > idleTime
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
    val manager = WindowManager.create(MainWindow, MainWindow.DefaultConfig) {
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

//TODO: Third Release:
// 2. Config: break interval and formula
// 3. Config: break duration
// 4. Config: break sound
// 5. Config: banned apps
// 8. Config: sound volume
// 9. Make packaging as MSI work (I think proguard is taking too much stuff)

// Get an array of graphics devices GraphicsDevice [] gs = ; // Loop through each device and print its size for (GraphicsDevice gd : gs) { // Get the display mode of the device DisplayMode dm = gd.getDisplayMode (); // Get the width and height of the device in pixels int width = dm.getWidth (); int height = dm.getHeight (); // Print the size System.out.println ("Monitor size: " + width + " x " + height); }}

