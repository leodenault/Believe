package believe.audio

import org.newdawn.slick.Music

/** Wraps [org.newdawn.slick.Music] to provide extra functionality. */
class Music(ref: String, val bpm: Int) : Music(ref) {
    fun paused(): Boolean = !playing() && position > 0
}