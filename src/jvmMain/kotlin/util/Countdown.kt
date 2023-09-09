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
class Countdown private constructor(private var intervalSeconds: Int, private val callback: () -> Unit) {
    companion object {
        fun start(intervalSeconds: Int, callback: () -> Unit) = Countdown(intervalSeconds, callback).apply {
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
        secondsLeft = intervalSeconds
        callback()
    }

//    private var startTime = System.currentTimeMillis()
//    private val timer = Timer()
//
//    private fun startTimer() {
//        timer.scheduleAtFixedRate(object : TimerTask() {
//            override fun run() {
//                startTime = System.currentTimeMillis()
//                callback()
//            }
//        }, totalDurationMs, Long.MAX_VALUE)
//    }
//
//    val timeLeft get() = (startTime + totalDurationMs - System.currentTimeMillis()).milliseconds
}

//class Countdown(private var startTime: Long, private var expectedEndTime: Long) {
//    companion object {
//        fun start(duration: Duration): Countdown {
//            val startTime = System.currentTimeMillis()
//            return Countdown(startTime, startTime + duration.inWholeMilliseconds)
//        }
//    }
//
//    private val callbacks = mutableListOf<() -> Unit>()
//    private var pauseTime: Long? = null
//
//    private val scope = CoroutineScope(Dispatchers.IO)
//
//    private var job = scope.launch {
//        waitUntilExpectedFinish()
//        callbacks.forEach { it() }
//    }
//
//    private suspend fun waitUntilExpectedFinish() {
//        delay(expectedEndTime - System.currentTimeMillis())
//        if (System.currentTimeMillis() <= expectedEndTime) {
//            waitUntilExpectedFinish()
//        }
//    }
//
//    fun pause() {
//        pauseTime = System.currentTimeMillis()
//    }
//
//    fun resume() {
//        val pTime = pauseTime ?: throw IllegalCallerException("pause() was not called before resuming with resume()")
//
//    }
//
//    fun onFinish(callback: () -> Unit) {
//        callbacks.add(callback)
//    }
//
//    fun forget(callback: () -> Unit) {
//        callbacks.remove(callback)
//    }
//
//    suspend fun waitUntilFinish() {
//
//    }
//}