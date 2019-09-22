package believe.map.collidable.command;

import static believe.util.MapEntry.entry;
import static com.google.common.truth.Truth.assertThat;

import believe.character.playable.PlayableCharacter;
import believe.geometry.Rectangle;
import believe.map.data.GeneratedMapEntityData;
import believe.map.tiled.EntityType;
import believe.map.tiled.TiledObject;
import believe.map.tiled.testing.FakeTiledMap;
import believe.physics.manager.PhysicsManageable;
import believe.testing.mockito.InstantiateMocksIn;
import believe.util.Util;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

/** Unit tests for {@link CommandGenerator}. */
@InstantiateMocksIn
public final class CommandGeneratorTest {
  private final GeneratedMapEntityData.Builder generatedMapEntityDataBuilder =
      GeneratedMapEntityData.newBuilder();

  private CommandGenerator commandGenerator;

  @Mock private CommandCollisionHandler<PlayableCharacter, Void> commandCollisionHandler;

  @BeforeEach
  void setUp() {
    commandGenerator =
        new CommandGenerator(
            Util.hashMapOf(entry("valid_command", CommandSupplier.from(commandCollisionHandler))),
            "command");
  }

  @Test
  void parseObject_generatesValidCommand() {
    FakeTiledMap tiledMap = FakeTiledMap.tiledMapWithObjectPropertyValue("valid_command");

    commandGenerator.parseObject(
        createTiledObject(
            tiledMap, /* x= */ 100, /* y= */ 200, /* width= */ 100, /* height= */ 200),
        generatedMapEntityDataBuilder);

    GeneratedMapEntityData generatedMapEntityData = generatedMapEntityDataBuilder.build();
    assertThat(generatedMapEntityData.physicsManageables()).hasSize(1);
    PhysicsManageable physicsManageable =
        generatedMapEntityData.physicsManageables().stream().findFirst().get();
    assertThat(physicsManageable).isInstanceOf(Command.class);
    @SuppressWarnings("unchecked")
    Command<?, Void> command = (Command<?, Void>) physicsManageable;
    Rectangle commandRect = command.rect();
    assertThat(commandRect.getX()).isWithin(0).of(100);
    assertThat(commandRect.getY()).isWithin(0).of(200);
    assertThat(commandRect.getWidth()).isWithin(0).of(100);
    assertThat(commandRect.getHeight()).isWithin(0).of(200);
    assertThat(command.leftCompatibleHandlers()).containsExactly(commandCollisionHandler);
  }

  @Test
  void parseObject_propertyCannotBeFound_doesNothing() {
    FakeTiledMap tiledMap = FakeTiledMap.tiledMapWithDefaultPropertyValues();

    commandGenerator.parseObject(createTiledObject(tiledMap), generatedMapEntityDataBuilder);

    GeneratedMapEntityData generatedMapEntityData = generatedMapEntityDataBuilder.build();
    assertThat(generatedMapEntityData.physicsManageables()).isEmpty();
    assertThat(generatedMapEntityData.updatables()).isEmpty();
  }

  @Test
  void parseObject_commandCannotBeFound_doesNothing() {
    commandGenerator.parseObject(
        createTiledObject(FakeTiledMap.tiledMapWithDefaultPropertyValues()),
        generatedMapEntityDataBuilder);

    GeneratedMapEntityData generatedMapEntityData = generatedMapEntityDataBuilder.build();
    assertThat(generatedMapEntityData.physicsManageables()).isEmpty();
    assertThat(generatedMapEntityData.updatables()).isEmpty();
  }

  private static TiledObject createTiledObject(FakeTiledMap fakeTiledMap) {
    return TiledObject.create(
        fakeTiledMap,
        EntityType.COLLIDABLE_TILE,
        /* x= */ 0,
        /* y= */ 0,
        /* width= */ 0,
        /* height= */ 0,
        /* layer= */ 0,
        /* tiledId= */ 0);
  }

  private static TiledObject createTiledObject(
      FakeTiledMap fakeTiledMap, int x, int y, int width, int height) {
    return TiledObject.create(
        fakeTiledMap,
        EntityType.COMMAND,
        /* x= */ x,
        /* y= */ y,
        /* width= */ width,
        /* height= */ height,
        /* layer= */ 0,
        /* tiledId= */ 0);
  }
}
