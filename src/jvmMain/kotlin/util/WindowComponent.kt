package util

import androidx.compose.runtime.*
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Tray
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import kotlin.system.exitProcess


data class WindowConfig(
    val visible: Boolean = true,
    val title: String = "untitled",
    val icon: Painter? = null,
    val undecorated: Boolean = false,
    val transparent: Boolean = false,
    val resizeable: Boolean = true,
    val enabled: Boolean = true,
    val focusable: Boolean = true,
    val alwaysOnTop: Boolean = false,
    val onPreviewKeyEvent: (KeyEvent) -> Boolean = { false },
    val onKeyEvent: (KeyEvent) -> Boolean = { false },
    val state: WindowState = WindowState(),
    val onCloseRequest: (context(ApplicationScope)() -> Unit)? = null
)

private data class ConfiguredWindow(val window: WindowComponent, val config: WindowConfig)

class WindowManager private constructor(private val initialWindows: List<ConfiguredWindow>) {
    companion object {
        context(ApplicationScope)
        @Composable
        fun display(initialWindow: WindowComponent, initialConfig: WindowConfig) {
            val manager = remember { WindowManager(listOf(ConfiguredWindow(initialWindow, initialConfig))) }
            CompositionLocalProvider(LocalWindowManager provides manager) {
                manager.showWindows()
            }
        }
    }

    private val activeWindows = mutableStateListOf(*initialWindows.toTypedArray())
    fun show(window: WindowComponent, config: WindowConfig) {
        if (activeWindows.none { it.window == window }) {
            activeWindows.add(ConfiguredWindow(window, config))
        }
    }

    private fun show(windows: List<ConfiguredWindow>) {
        val activeWindowContents = activeWindows.map { it.window }.toHashSet()
        activeWindows.addAll(windows.filter { it.window !in activeWindowContents })
    }

    fun hide(window: WindowComponent) {
        activeWindows.removeIf { it.window == window }
    }

    context(ApplicationScope)
    @Composable
    private fun showWindows() {
        Tray(
            icon = painterResource("icon.png"),
            menu = {
                Item(
                    "Exit",
                    onClick = { exitProcess(1) }
                )
            },
            onAction = {
                show(initialWindows)
            }
        )

        for ((window, config) in activeWindows) {
            Window(
                visible = config.visible,
                undecorated = config.undecorated,
                transparent = config.transparent,
                alwaysOnTop = config.alwaysOnTop,
                enabled = config.enabled,
                focusable = config.focusable,
                icon = config.icon,
                onKeyEvent = config.onKeyEvent,
                state = config.state,
                title = config.title,
                content = { window.content() },
                onPreviewKeyEvent = config.onPreviewKeyEvent,
                onCloseRequest = {
                    if (config.onCloseRequest == null) hide(window)
                    else config.onCloseRequest.invoke(this@ApplicationScope)
                }
            )
        }
    }
}

val LocalWindowManager = compositionLocalOf<WindowManager> { error("Window Manager not found") }


interface WindowComponent {
    @Composable
    fun content()
}