package believe.map.collidable.command;

import static com.google.common.truth.Truth.assertThat;

import believe.geometry.Rectangle;
import believe.map.tiled.EntityType;
import believe.map.tiled.TiledObject;
import believe.map.tiled.testing.FakeTiledMap;
import believe.testing.mockito.InstantiateMocksIn;
import com.google.common.truth.Truth8;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

/** Unit tests for {@link Command}. */
@InstantiateMocksIn
public final class CommandTest {
  private static final TiledObject TILED_OBJECT =
      TiledObject.create(
          FakeTiledMap.tiledMapWithDefaultPropertyValues(),
          EntityType.COMMAND,
          /* x= */ 123,
          /* y= */ 321,
          /* width= */ 10,
          /* height= */ 10,
          /* layerId= */ 0,
          /* objectId= */ 0);
  private Command<?, String> command;

  @Mock private CommandCollisionHandler<?, String> commandCollisionHandler;

  @BeforeEach
  void setUp() {
    command = Command.create(commandCollisionHandler, TILED_OBJECT);
  }

  @Test
  void getRect_returnsRectangleWithSameDimensionsAsConstructor() {
    Rectangle rectangle = command.rect();

    assertThat(rectangle.getX()).isWithin(0).of(123);
    assertThat(rectangle.getY()).isWithin(0).of(321);
    assertThat(rectangle.getWidth()).isWithin(0).of(10);
    assertThat(rectangle.getHeight()).isWithin(0).of(10);
  }

  @Test
  void leftCompatibleHandlers_returnsProvidedCollisionHandler() {
    assertThat(command.leftCompatibleHandlers()).containsExactly(commandCollisionHandler);
  }

  @Test
  void data_wasNeverSpecified_returnsEmpty() {
    Truth8.assertThat(command.data()).isEmpty();
  }

  @Test
  void data_wasSpecified_returnsData() {
    Truth8.assertThat(Command.create(commandCollisionHandler, TILED_OBJECT, "my data").data())
        .hasValue("my data");
  }
}
