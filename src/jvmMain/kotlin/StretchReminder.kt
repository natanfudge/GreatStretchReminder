import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FloatTweenSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import util.*
import java.awt.Toolkit

object StretchReminder : WindowComponent {
    fun show(window: WindowManager) {
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
    }
    context(BoxScope)
    @Composable
    override fun content() {
        val window = LocalWindowManager.current
        Box(Modifier.fillMaxSize().background(MaterialTheme.colors.background.copy(alpha = 0.3f))) {
            Dialog(
                onCloseRequest = { window.hide(StretchReminder) },
                undecorated = true,
                transparent = true,
                state = DialogState(size = DpSize(400.dp, 200.dp))
            ) {
                StretchDialog()
            }
        }
    }
}

private const val breakSeconds = 60
context(WindowScope)
@Composable
private fun StretchDialog() {
    val window = LocalWindowManager.current
    WithTitleBar(onCloseRequest = { window.hide(StretchReminder) }, "Great Stretch Reminder") {
        Box(Modifier.fillMaxSize().background(MaterialTheme.colors.background)) {
            Column(Modifier.padding(5.dp).align(Alignment.Center)) {
                val progress = remember {Animatable(0f) }
                LaunchedEffect(Unit) {
                    progress.animateTo(1f, animationSpec = FloatTweenSpec(duration = breakSeconds * 10_000, easing = LinearEasing))
                    window.hide(StretchReminder)
                }
                Text(
                    "This is a reminder to go pet a cat",
                )
                LinearProgressIndicator(progress.value, Modifier.fillMaxWidth().padding(vertical = 10.dp).height(30.dp))
                Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
                    val secondsRemaining = (1 - progress.value) * breakSeconds
                    Text("Time remaining: ${formatSeconds(secondsRemaining.toInt())}")
                    OutlinedButton(
                        onClick = { window.hide(StretchReminder) },
                    ) {
                        Text("Close")
                    }
                }

            }
        }
    }
}


