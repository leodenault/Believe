package believe.map.collidable.command;

import static believe.util.MapEntry.entry;
import static com.google.common.truth.Truth.assertThat;

import believe.geometry.Rectangle;
import believe.map.data.GeneratedMapEntityData;
import believe.map.tiled.EntityType;
import believe.map.tiled.Tile;
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

  @Mock private CommandCollisionHandler<?> commandCollisionHandler;

  @BeforeEach
  void setUp() {
    commandGenerator =
        new CommandGenerator(
            Util.hashMapOf(entry("valid_command", commandCollisionHandler)), "command");
  }

  @Test
  void parseTile_generatesValidCommand() {
    FakeTiledMap tiledMap = FakeTiledMap.tiledMapWithTilePropertyValue("valid_command");

    commandGenerator.parseTile(
        tiledMap,
        createTileData(
            tiledMap,
            /* pixelX= */ 100,
            /* pixelY= */ 200,
            /* tileWidth= */ 100,
            /* tileHeight= */ 200
            /* layer= */ ),
        generatedMapEntityDataBuilder);

    GeneratedMapEntityData generatedMapEntityData = generatedMapEntityDataBuilder.build();
    assertThat(generatedMapEntityData.physicsManageables()).hasSize(1);
    PhysicsManageable physicsManageable =
        generatedMapEntityData.physicsManageables().stream().findFirst().get();
    assertThat(physicsManageable).isInstanceOf(Command.class);
    Command<?> command = (Command<?>) physicsManageable;
    Rectangle commandRect = command.rect();
    assertThat(commandRect.getX()).isWithin(0).of(100);
    assertThat(commandRect.getY()).isWithin(0).of(200);
    assertThat(commandRect.getWidth()).isWithin(0).of(100);
    assertThat(commandRect.getHeight()).isWithin(0).of(200);
    assertThat(command.leftCompatibleHandlers()).containsExactly(commandCollisionHandler);
  }

  @Test
  void parseTile_propertyCannotBeFound_doesNothing() {
    FakeTiledMap tiledMap = FakeTiledMap.tiledMapWithDefaultTilePropertyValue();

    commandGenerator.parseTile(tiledMap, createTileData(tiledMap), generatedMapEntityDataBuilder);

    GeneratedMapEntityData generatedMapEntityData = generatedMapEntityDataBuilder.build();
    assertThat(generatedMapEntityData.physicsManageables()).isEmpty();
    assertThat(generatedMapEntityData.updatables()).isEmpty();
  }

  @Test
  void parseTile_commandCannotBeFound_doesNothing() {
    FakeTiledMap tiledMap = FakeTiledMap.tiledMapWithTilePropertyValue("does_not_exist");

    commandGenerator.parseTile(
        tiledMap,
        createTileData(FakeTiledMap.tiledMapWithDefaultTilePropertyValue()),
        generatedMapEntityDataBuilder);

    GeneratedMapEntityData generatedMapEntityData = generatedMapEntityDataBuilder.build();
    assertThat(generatedMapEntityData.physicsManageables()).isEmpty();
    assertThat(generatedMapEntityData.updatables()).isEmpty();
  }

  private static Tile createTileData(FakeTiledMap fakeTiledMap) {
    return Tile.create(
        fakeTiledMap,
        EntityType.COLLIDABLE_TILE,
        /* tiledId= */ 0,
        /* tileX= */ 0,
        /* tileY= */ 0,
        /* tileWidth= */ 0,
        /* tileHeight= */ 0,
        /* layer= */ 0);
  }

  private static Tile createTileData(
      FakeTiledMap fakeTiledMap, int pixelX, int pixelY, int tileWidth, int tileHeight) {
    return Tile.create(
        fakeTiledMap,
        EntityType.COMMAND,
        /* tiledId= */ 0,
        /* tileX= */ pixelX / tileWidth,
        /* tileY= */ pixelY / tileHeight,
        /* tileWidth= */ tileWidth,
        /* tileHeight= */ tileHeight,
        /* layer= */ 0);
  }
}
