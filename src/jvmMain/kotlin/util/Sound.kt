package util

import App
import java.io.BufferedInputStream
import java.io.File
import javax.sound.sampled.AudioSystem

fun playSound(resource: String) {
    // Get the audio input stream from the resource file
    val audioInputStream = AudioSystem.getAudioInputStream(BufferedInputStream(App::class.java.getResourceAsStream(resource)))
    // Get a clip object to play the sound
    val clip = AudioSystem.getClip()
    // Open the clip with the audio input stream
    clip.open(audioInputStream)
    // Start playing the sound
    clip.start()
}