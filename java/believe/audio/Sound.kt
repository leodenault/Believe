package believe.audio

import org.newdawn.slick.SlickException
import org.newdawn.slick.Sound
import org.newdawn.slick.util.Log

/** Wrapper for [org.newdawn.slick.Sound] instances. */
class Sound(location: String) {
    private var slickSound: Sound? = null

    init {
        try {
            slickSound = Sound(location)
        } catch (e: SlickException) {
            Log.error("Could not load sound at '$location'.", e)
        }
    }

    fun play() = slickSound?.play()
}