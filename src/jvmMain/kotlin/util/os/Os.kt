package util.os

import com.sun.jna.platform.win32.Kernel32
import java.lang.IllegalStateException
import java.util.*


object Os {
    fun anyAreRunning(apps: List<String>): Boolean {
        // Get the list of running processes on Windows
        val process = Runtime.getRuntime().exec(System.getenv("windir") + "\\system32\\" + "tasklist.exe")
        val scanner = Scanner(process.inputStream)
        while (scanner.hasNextLine()) {
            val line: String = scanner.nextLine()
            if (apps.any { line.startsWith(it) }) {
                return true
            }
        }

        return false
    }

    private val startTicks = lastActiveTimeMs()
    private val startUnixMs = System.currentTimeMillis()
    // This is how far removed the windows "ticks" are from unix ms
    private val ticksToUnixDiscrepancy = startUnixMs - startTicks

    /**
     * It's not that precise, I say it's about in the range of 1000ms precise. Returns relative to unix time
     */
    fun lastActiveTimeMs(): Long {
        val lii = User32.LASTINPUTINFO()

        if (User32.INSTANCE.GetLastInputInfo(lii)) {
            return lii.dwTime + ticksToUnixDiscrepancy
        } else {
            val errorCode = Kernel32.INSTANCE.GetLastError()
            throw IllegalStateException("Failed to run Windows GetLastInputInfo, error: $errorCode")
        }
    }
}