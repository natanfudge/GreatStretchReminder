package util

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.key.KeyEvent
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.window.ApplicationScope
import androidx.compose.ui.window.Tray
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowScope
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

typealias TrayFunc = @Composable context(ApplicationScope, WindowManager) () -> Unit

class WindowManager private constructor(private val initialWindows: List<ConfiguredWindow>, private val tray:  TrayFunc) {
    companion object {
        fun create(initialWindow: WindowComponent, initialConfig: WindowConfig, tray: TrayFunc): WindowManager {
            return WindowManager(listOf(ConfiguredWindow(initialWindow, initialConfig)), tray)
        }
        context(ApplicationScope)
        @Composable
        fun display(manager: WindowManager) {
            CompositionLocalProvider(LocalWindowManager provides manager) {
                manager.showWindows()
            }
        }
//
//
//        @Composable
//        fun display(initialWindow: WindowComponent, initialConfig: WindowConfig): WindowManager {
//            val manager =
//                remember { WindowManager(listOf(ConfiguredWindow(initialWindow, initialConfig))) }
//
//            return manager
//        }
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
        tray(this@ApplicationScope, this@WindowManager)

        for ((window, config) in activeWindows) {
            val onCloseRequest = {
                if (config.onCloseRequest == null) hide(window)
                else config.onCloseRequest.invoke(this@ApplicationScope)
            }
            Window(
                visible = config.visible,
//                undecorated = config.undecorated,
//                transparent = config.transparent,
                transparent = true,
                undecorated = true,
                alwaysOnTop = config.alwaysOnTop,
                enabled = config.enabled,
                focusable = config.focusable,
                icon = config.icon,
                onKeyEvent = config.onKeyEvent,
                state = config.state,
                title = config.title,
                content = {
                    WithDecoration(config.undecorated, onCloseRequest) {
                        WithBackground(config.transparent) {
                            window.content()
                        }
                    }
                },
                onPreviewKeyEvent = config.onPreviewKeyEvent,
                onCloseRequest = onCloseRequest
            )
        }
    }
}

context(WindowScope)
@Composable
private fun WithDecoration(
    undecorated: Boolean,
    onCloseRequest: () -> Unit,
    content: @Composable () -> Unit
) {
    if (undecorated) {
        content()
    } else {
        WithTitleBar(onCloseRequest) {
            content()
        }
    }
}

context(WindowScope)
@Composable
private fun WithBackground(transparent: Boolean, content: WindowContent) {
    if (transparent) {
        Box {
            content(this)
        }

    } else {
        Box(Modifier.fillMaxSize().background(MaterialTheme.colors.background)) {
            content(this)
        }
    }
}

val LocalWindowManager = compositionLocalOf<WindowManager> { error("Window Manager not found") }

typealias WindowContent =  @Composable context(BoxScope)() -> Unit


interface WindowComponent {
    context(BoxScope)
    @Composable
    fun content()
}