import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FloatTweenSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import util.*
import java.awt.Toolkit
import kotlin.random.Random

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
            DialogWindow(
                onCloseRequest = { window.hide(StretchReminder) },
                state = DialogState(size = DpSize(400.dp, 200.dp)),
                undecorated = true,
                transparent = true
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
    LaunchedEffect(Unit) {
        playSound("/tuturu.wav")
    }

    val window = LocalWindowManager.current
    WithTitleBar(onCloseRequest = null, "Great Stretch Reminder") {
        Box(Modifier.fillMaxSize().background(MaterialTheme.colors.background)) {
            Column(Modifier.padding(5.dp).align(Alignment.Center)) {
                val progress = remember { Animatable(0f) }
                LaunchedEffect(Unit) {
                    progress.animateTo(1f, animationSpec = FloatTweenSpec(duration = breakSeconds * 1_000, easing = LinearEasing))
                    window.hide(StretchReminder)
                }
                Text(
                    "This is a reminder to go pet a cat"
                )
                LinearProgressIndicator(progress.value, Modifier.fillMaxWidth().padding(vertical = 10.dp).height(30.dp))
                val secondsRemaining = (1 - progress.value) * breakSeconds
                Text("Time remaining: ${formatSeconds(secondsRemaining.toInt())}")
                SkipStretchPuzzle { window.hide(StretchReminder) }
//                Row(
//                    horizontalArrangement = Arrangement.SpaceBetween,
//                    verticalAlignment = Alignment.CenterVertically,
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//
//                    OutlinedButton(
//                        onClick = { window.hide(StretchReminder) },
//                    ) {
//                        Text("Close")
//                    }
//                }

            }
        }
    }
}


@Composable
fun SkipStretchPuzzle(onSolve: () -> Unit) {
    val factor1 = remember { Random.nextInt(11, 99) }
    val factor2 = remember { Random.nextInt(11, 99) }
    var answerText by remember { mutableStateOf("") }
    TextField(
        value = answerText,
        onValueChange = {
            if (it.toIntOrNull() == factor1 * factor2) onSolve()
            answerText = it
        },
        modifier = Modifier.fillMaxWidth().padding(5.dp),
        label = { Text("Solve: $factor1 X $factor2") }
    )
}