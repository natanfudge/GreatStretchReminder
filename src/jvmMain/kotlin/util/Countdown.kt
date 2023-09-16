package util

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * A not-very-precise countdown that works off of second-based intervals.
 * The benefit is that it uses a MutableState that can be used in GUI code.
 */
class Countdown private constructor(var intervalSeconds: Int, private val callback: suspend () -> Unit) {
    companion object {
        fun start(intervalSeconds: Int, callback: suspend () -> Unit) = Countdown(intervalSeconds, callback).apply {
            tick()
        }
    }

    private val scope = CoroutineScope(Dispatchers.IO)
    var secondsLeft by mutableStateOf(intervalSeconds)

    private fun tick() {
        scope.launch {
            while (true) {
                delay(1000)
                secondsLeft--
                if (secondsLeft == 0) {
                    callback()
                    secondsLeft = intervalSeconds
                }

            }
        }
    }

    fun reset() {
        secondsLeft = intervalSeconds
    }


    fun end() {
        scope.launch {
            callback()
            secondsLeft = intervalSeconds
        }
    }
}
