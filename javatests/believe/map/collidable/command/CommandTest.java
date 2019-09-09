package believe.map.collidable.command;

import static com.google.common.truth.Truth.assertThat;

import believe.geometry.Rectangle;
import believe.map.tiled.EntityType;
import believe.map.tiled.Tile;
import believe.map.tiled.testing.FakeTiledMap;
import believe.testing.mockito.InstantiateMocksIn;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

/** Unit tests for {@link Command}. */
@InstantiateMocksIn
public final class CommandTest {
  private Command<?> command;

  @Mock private CommandCollisionHandler<?> commandCollisionHandler;

  @BeforeEach
  void setUp() {
    command =
        new Command<>(
            commandCollisionHandler,
            Tile.create(
                FakeTiledMap.tiledMapWithDefaultPropertyValues(),
                EntityType.COMMAND,
                /* tileId= */ 0,
                /* x= */ 123,
                /* y= */ 321,
                /* width= */ 10,
                /* height= */ 10,
                /* layerId= */ 0));
  }

  @Test
  void getRect_returnsRectangleWithSameDimensionsAsConstructor() {
    Rectangle rectangle = command.rect();

    assertThat(rectangle.getX()).isWithin(0).of(1230);
    assertThat(rectangle.getY()).isWithin(0).of(3210);
    assertThat(rectangle.getWidth()).isWithin(0).of(10);
    assertThat(rectangle.getHeight()).isWithin(0).of(10);
  }

  @Test
  void leftCompatibleHandlers_returnsProvidedCollisionHandler() {
    assertThat(command.leftCompatibleHandlers()).containsExactly(commandCollisionHandler);
  }
}
