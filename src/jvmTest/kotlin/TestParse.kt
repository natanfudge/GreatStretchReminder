
import model.importWorkouts
import java.nio.file.Paths
import kotlin.test.Test

class TestParse  {
    @Test
    fun testParse() {
        val workouts = importWorkouts(Paths.get("Keep"))
        val x = 2
    }

    @Test
    fun testGraph() {

    }
}