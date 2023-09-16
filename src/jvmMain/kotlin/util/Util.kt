package util

import java.util.*
import kotlin.concurrent.schedule
import kotlin.time.Duration

inline fun every(time: Long, crossinline func: () -> Unit) {
    Timer().schedule(time, Long.MAX_VALUE) {
        func()
    }
}