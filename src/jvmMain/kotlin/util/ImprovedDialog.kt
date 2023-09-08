package util

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.window.WindowDraggableArea
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.sharp.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogWindowScope
import androidx.compose.ui.window.WindowScope

context(WindowScope)
@Composable
fun WithTitleBar(
    onCloseRequest: () -> Unit,
    title: String = "untitled",
    content: @Composable context(ColumnScope) () -> Unit
) {
//    Card(Modifier.fillMaxSize(), , backgroundColor = Color.Transparent, contentColor = Color.Transparent) {
        Column(Modifier.fillMaxSize().clip(RoundedCornerShape(10.dp)) ) {
            DialogTitleBar(onCloseRequest, title)
            content(this)
        }
//    }

}

@Composable
private fun WindowScope.DialogTitleBar(
    onCloseRequest: () -> Unit,
    title: String
) = WindowDraggableArea {
//    Column(modifier = Modifier.fillMaxWidth()) {
//    Surface(
//        Modifier.fillMaxWidth()
//            .background(),
//        elevation = 15.dp,
//    ) {
        Row(
            modifier = Modifier.fillMaxWidth().background(color = MaterialTheme.colors.surface.copy(alpha = 0.8f)),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
//            Box(Modifier.size(40.dp))
            Text(
                modifier = Modifier.padding(start = 12.dp),
                color = MaterialTheme.colors.onBackground.copy(alpha = 0.5f),
//                color = MaterialTheme.colors.primary,
                fontSize = MaterialTheme.typography.body2.fontSize,
                text = title
            )

            CloseButton(onCloseRequest)
        }
//    }

//        Divider(
//            Modifier.fillMaxWidth(),
//            color = MaterialTheme.colors.primary.copy(alpha = .95f)
//        )
//    }
}

@Composable
fun CloseButton(onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()
    Icon(
        modifier = Modifier
            .let { if (isHovered) it.background(Color.Red) else it }
            .padding(5.dp)
            .size(24.dp)
            .hoverable(interactionSource)
            .clickable(onClick = onClick),
        imageVector = Icons.Outlined.Close,
        contentDescription = "Close",
        tint = if(isHovered)  MaterialTheme.colors.onBackground else MaterialTheme.colors.onBackground.copy(alpha = 0.5f)
    )
}