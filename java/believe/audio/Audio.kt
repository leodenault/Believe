package believe.audio

import believe.datamodel.LoadableData
import org.newdawn.slick.SlickException
import org.newdawn.slick.util.Log

@JvmOverloads
fun soundFrom(
    fileLocation: String,
    provideSlickSound: (String) -> org.newdawn.slick.Sound = { org.newdawn.slick.Sound(it) }
): LoadableData<Sound> = object : LoadableData<Sound> {
    override fun load(): Sound? = try {
        object : Sound {
            private val slickSound = provideSlickSound(fileLocation)

            override fun play() {
                slickSound.play()
            }
        }
    } catch (e: SlickException) {
        Log.error("Could not load sound at '$fileLocation'.")
        null
    }
}

@JvmOverloads
fun musicFrom(
    fileLocation: String,
    provideSlickMusic: (String) -> org.newdawn.slick.Music = { org.newdawn.slick.Music(it) }
): LoadableData<Music> = object : LoadableData<Music> {
    override fun load(): Music? = try {
        object : Music {
            private val slickMusic = provideSlickMusic(fileLocation)
            override val position: Float
                get() = slickMusic.position

            override fun loop() = slickMusic.loop()

            override fun stop() = slickMusic.stop()

            override fun pause() = slickMusic.pause()

            override fun resume() = slickMusic.resume()

            override fun isPaused(): Boolean = !slickMusic.playing() && slickMusic.position > 0

            override fun play() = slickMusic.play()

            override fun isPlaying(): Boolean = slickMusic.playing()
        }
    } catch (e: SlickException) {
        Log.error("Could not load music at '$fileLocation'.")
        null
    }
}
