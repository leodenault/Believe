package believe.audio

/** A [Sound] used for playing background music. */
interface Music : Sound {
    /**
     * The current progress of the music where 0 is at the beginning of the content and 1.0f is at
     * the end.
     */
    val position: Float

    /** Plays this music instance and starts at the beginning when the end is reached. */
    fun loop()

    /** Stops playing the music. If playing the music again, it will start from the beginning. */
    fun stop()

    /**
     * Stops playing the music. The music may be resumed at the same position it was left off of by
     * calling [resume].
     */
    fun pause()

    /** Resumes playing the music at whatever point it was left when it was last paused. */
    fun resume()

    /** Returns whether the music is paused or not. */
    fun isPaused(): Boolean

    /** Returns whether the music is currently playing audio. */
    fun isPlaying(): Boolean
}
