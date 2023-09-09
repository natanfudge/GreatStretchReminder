import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
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

context(BoxScope, DialogWindowScope)
@Composable
private fun StretchDialog() {
    val window = LocalWindowManager.current
    WithTitleBar(onCloseRequest = { window.hide(StretchReminder) }, "Great Stretch Reminder") {
        Box(Modifier.fillMaxSize().background(MaterialTheme.colors.background)) {
            Column(Modifier.padding(5.dp).align(Alignment.Center)) {
                Text(
                    "This is a reminder to go pet a cat",
                    color = MaterialTheme.colors.onBackground
                )
                OutlinedButton(
                    onClick = { window.hide(StretchReminder) },
                    Modifier.align(Alignment.CenterHorizontally)
                ) {
                    Text("Close")
                }
            }
        }
    }
}


