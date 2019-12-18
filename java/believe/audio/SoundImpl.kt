package believe.audio

import org.newdawn.slick.SlickException
import org.newdawn.slick.util.Log

/** Wrapper for [org.newdawn.slick.Sound] instances. */
class SoundImpl(location: String) : Sound {
    private val slickSound: org.newdawn.slick.Sound?

    init {
        slickSound = try {
            org.newdawn.slick.Sound(location)
        } catch (e: SlickException) {
            Log.error("Could not load sound at '$location'.", e)
            null
        }
    }

    override fun play() {
        slickSound?.play()
    }
}