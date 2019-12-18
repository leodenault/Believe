package believe.audio.testing

import believe.audio.Sound

/** A fake implementation of [Sound] for use in testing. */
class FakeSound: Sound {
    override fun play() {
    }
}