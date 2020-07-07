package believe.scene

import believe.core.Updatable
import believe.core.display.Bindable
import believe.core.display.RenderableV2

interface SceneElement : RenderableV2, Bindable, Updatable {
    var x: Float
    var y: Float
}