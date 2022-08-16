package believe.audio

import believe.datamodel.LoadableData
import dagger.Reusable
import org.newdawn.slick.SlickException
import org.newdawn.slick.util.Log
import javax.inject.Inject

class LoadableSound private constructor(
    val provideSlickSound: (String) -> org.newdawn.slick.Sound,
    val fileLocation: String
) : LoadableData<Sound> {
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

    @Reusable
    internal class Factory @Inject constructor() {
        private var provideSlickSound: (String) -> org.newdawn.slick.Sound =
            { org.newdawn.slick.Sound(it) }

        constructor(provideSlickSound: (String) -> org.newdawn.slick.Sound) : this() {
            this.provideSlickSound = provideSlickSound
        }

        fun create(fileLocation: String): LoadableSound =
            LoadableSound(provideSlickSound, fileLocation)
    }
}
