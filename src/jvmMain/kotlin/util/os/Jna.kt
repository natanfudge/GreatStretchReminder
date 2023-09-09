package util.os

import com.sun.jna.Library
import com.sun.jna.Native
import com.sun.jna.Structure


interface User32 : Library {
    companion object {
        val INSTANCE = Native.load("user32", User32::class.java)
    }


    // Define the GetLastInputInfo function
    fun GetLastInputInfo(plii: LASTINPUTINFO?): Boolean

    // Define the structure LASTINPUTINFO
    class LASTINPUTINFO : Structure() {
        @JvmField
        var cbSize = 8 // Size of the structure in bytes
        @JvmField
        var dwTime = 0 // Time of the last input event (milliseconds)

        override fun getFieldOrder(): MutableList<String> {
            return mutableListOf("cbSize", "dwTime")
        }
    }


}
