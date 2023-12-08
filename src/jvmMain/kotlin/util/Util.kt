package util

import java.util.*
import kotlin.concurrent.schedule

inline fun every(time: Long, crossinline func: () -> Unit) {
    Timer().schedule(delay = time, period = time) {
        func()
    }
}