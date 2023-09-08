import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import util.LocalWindowManager
import util.WindowComponent

object StretchReminder : WindowComponent {
    @Composable
    override fun content() {
        val window = LocalWindowManager.current
        Box(Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.6f))) {
            Card(Modifier.align(Alignment.Center)) {
                Column(Modifier.padding(5.dp)) {
                    Text("This is a reminder to go pet a cat", )
                    Button(onClick = { window.hide(StretchReminder) }) {
                        Text("Close")
                    }
                }
            }
        }
    }
}