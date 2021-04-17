package believe.character

import believe.character.proto.CharacterAnimationsProto
import believe.datamodel.MutableValue
import believe.map.data.EntityType
import believe.map.data.ObjectFactory
import believe.scene.GeneratedMapEntityData
import believe.map.io.ObjectParser
import believe.map.tiled.TiledObject
import believe.physics.manager.PhysicsManageable
import dagger.Reusable
import org.newdawn.slick.util.Log
import javax.inject.Inject

@Reusable
internal class PlayerObjectParser @Inject internal constructor(
    private val characterFactory: CharacterV2.Factory, private val parseAnimations: (
        @JvmSuppressWildcards CharacterAnimationsProto.CharacterAnimations
    ) -> @JvmSuppressWildcards Animations, private val mutablePlayer: MutableValue<CharacterV2>
) : ObjectParser {

    override fun parseObject(tiledObject: TiledObject): ObjectFactory {
        val playerName = tiledObject.name?.toLowerCase() ?: return ObjectFactory.EMPTY.also {
            Log.error("Expected player object to have a name.")
        }
        val animationData =
            CharacterAnimationsProto.CharacterAnimations.newBuilder().setSpriteSheetName(
                playerName
            ).setIdleAnimationName(
                "idle"
            ).setMovementAnimationName("moving").setJumpAnimationName("jumping").build()

        return ObjectFactory { generatedMapEntityDataBuilder ->
            val player = characterFactory.create(
                parseAnimations(animationData), tiledObject.x, tiledObject.y, Faction.GOOD
            )
            generatedMapEntityDataBuilder.addPhysicsManageable(PhysicsManageable {
                it.addCollidable(player)
                it.addGravityObject(player)
            })
            generatedMapEntityDataBuilder.addSceneElement(player)
            mutablePlayer.update(player)
        }
    }
}