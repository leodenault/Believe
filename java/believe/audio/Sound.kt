package believe.audio

/** A relatively short audio clip used in sound effects and the like. */
interface Sound {
    /** Plays back the sound represented by this instance. */
    fun play()

    companion object {
        val EMPTY = object : Sound {
            override fun play() {}
        }
    }
}
