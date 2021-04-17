package believe.map.data.testing

import believe.map.data.ObjectFactory
import com.google.common.truth.Truth

object Truth {
    /** Begins an assertion about `objectFactory`. */
    @JvmStatic
    fun assertThat(objectFactory: ObjectFactory): ObjectFactorySubject =
        Truth.assertAbout(ObjectFactorySubject.objectFactories()).that(objectFactory)
}
