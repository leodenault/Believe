package believe.character

import believe.character.proto.CharacterAnimationsProto
import believe.datamodel.MutableValue
import believe.map.data.EntityType
import believe.map.data.GeneratedMapEntityData
import believe.map.io.ObjectParser
import believe.map.tiled.TiledObject
import dagger.Reusable
import org.newdawn.slick.util.Log
import javax.inject.Inject

@Reusable
internal class PlayerObjectParser @Inject internal constructor(
    private val characterFactory: CharacterV2.Factory, private val parseAnimations: (
        @JvmSuppressWildcards CharacterAnimationsProto.CharacterAnimations
    ) -> @JvmSuppressWildcards Animations, private val mutablePlayer: MutableValue<CharacterV2>
) : ObjectParser {

    override fun parseObject(
        entityType: EntityType,
        tiledObject: TiledObject,
        generatedMapEntityDataBuilder: GeneratedMapEntityData.Builder
    ) {
        if (entityType != EntityType.PLAYER) return

        val playerName = tiledObject.name?.toLowerCase()
            ?: return Unit.also { Log.error("Expected player object to have a name.") }
        val player = characterFactory.create(
            parseAnimations(
                CharacterAnimationsProto.CharacterAnimations.newBuilder().setSpriteSheetName(
                    playerName
                ).setIdleAnimationName(
                    "idle"
                ).setMovementAnimationName("moving").setJumpAnimationName("jumping").build()
            ), CharacterV2.DamageListener.NONE, tiledObject.x, tiledObject.y, Faction.GOOD
        )
        generatedMapEntityDataBuilder.addPhysicsManageable {
            it.addCollidable(player)
            it.addGravityObject(player)
        }
        generatedMapEntityDataBuilder.addSceneElement(player)
        mutablePlayer.update(player)
    }
}