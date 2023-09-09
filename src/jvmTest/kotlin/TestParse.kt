import kotlinx.coroutines.*
import java.util.concurrent.CancellationException
import kotlin.test.Test

class TestParse {
    @Test
    fun testParse() {
        val scope = CoroutineScope(Dispatchers.IO)

        scope.launch {

            delay(500)
            println("Cancelling...")

            scope.launch {
//                try {
                    delay(3000)
                    println("Got to the end")
//                } catch (e: CancellationException) {
                    println("Caught cancellation")
                    scope.launch {
                        delay(500)
                        println("Halo second time")
                    }
                }
//            }
            scope.cancel("foo")
        }

        runBlocking {
            delay(4000)
        }
    }

    @Test
    fun testGraph() {

    }
}