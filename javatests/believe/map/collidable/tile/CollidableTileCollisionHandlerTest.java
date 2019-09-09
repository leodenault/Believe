package believe.map.collidable.tile;

import static believe.map.collidable.tile.CollidableTileCollisionHandlerTest.FakeTileCollidable.Subject.assertThat;
import static com.google.common.truth.Truth.assertAbout;

import believe.geometry.Rectangle;
import believe.map.collidable.tile.CollidableTileCollisionHandler.TileCollidable;
import believe.map.tiled.EntityType;
import believe.map.tiled.Tile;
import believe.map.tiled.testing.FakeTiledMap;
import believe.physics.collision.Collidable;
import believe.physics.collision.CollisionHandler;
import com.google.common.truth.FailureMetadata;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.Set;

public class CollidableTileCollisionHandlerTest {
  static final class FakeTileCollidable implements TileCollidable<FakeTileCollidable> {
    static final class Subject
        extends com.google.common.truth.Subject<Subject, FakeTileCollidable> {
      private Subject(FailureMetadata failureMetadata, FakeTileCollidable fakeTileCollidable) {
        super(failureMetadata, fakeTileCollidable);
      }

      static Factory<Subject, FakeTileCollidable> fakeTileCollidables() {
        return Subject::new;
      }

      static Subject assertThat(FakeTileCollidable fakeTileCollidable) {
        return assertAbout(fakeTileCollidables()).that(fakeTileCollidable);
      }

      void isLocatedAt(float x, float y) {
        check().withMessage("x value is different").that(actual().rect.getX()).isEqualTo(x);
        check().withMessage("y value is different").that(actual().rect.getY()).isEqualTo(y);
      }

      void isLanded() {
        check().that(actual().isLanded).isTrue();
      }
    }

    final Rectangle rect;
    boolean isLanded;

    private float verticalSpeed;

    FakeTileCollidable(float x1, float y1, float x2, float y2) {
      rect = new Rectangle(x1, y1, x2 - x1, y2 - y1);
      isLanded = false;
    }

    @Override
    public void landed() {
      isLanded = true;
    }

    @Override
    public Set<CollisionHandler<? super FakeTileCollidable, ? extends Collidable<?>>>
        leftCompatibleHandlers() {
      return Collections.emptySet();
    }

    @Override
    public Set<CollisionHandler<? extends Collidable<?>, ? super FakeTileCollidable>>
        rightCompatibleHandlers() {
      return Collections.emptySet();
    }

    @Override
    public Rectangle rect() {
      return rect;
    }

    @Override
    public float getVerticalSpeed() {
      return verticalSpeed;
    }

    @Override
    public void setVerticalSpeed(float speed) {
      this.verticalSpeed = speed;
    }

    @Override
    public void setLocation(float x, float y) {
      rect.setLocation(x, y);
    }

    @Override
    public float getFloatX() {
      return rect.getX();
    }

    @Override
    public float getFloatY() {
      return rect.getY();
    }
  }

  //         upTile
  //            \
  //             *--------*    Subject
  //             |        |   /
  // leftTile *--|--------|--*
  //   \      |  |        |  |
  //    *-----|--*--------*--|-----*
  //    |     |  |xxxxxxxx|  |     |
  //    |     |  |xxxxxxxx|  |     |
  //    |     |  |xxxxxxxx|  |     |
  //    *-----|--*--------*--|-----*
  //          |  |        |  |      \
  //          *--|--------|--*     rightTile
  //             |        |
  //             *--------*
  //                       \
  //                      downTile

  private static final int TILE_LENGTH = 100;

  private final CollidableTileCollisionHandler handler = new CollidableTileCollisionHandler();
  private final FakeTileCollidable subject = new FakeTileCollidable(80, 80, 220, 220);
  private final CollidableTile leftTile =
      new CollidableTile(
          Tile.create(
              FakeTiledMap.tiledMapWithDefaultPropertyValues(),
              EntityType.COLLIDABLE_TILE,
              /* tileId= */ 0,
              /* x= */ 0,
              /* y= */ 1,
              /* width= */ TILE_LENGTH,
              /* height= */ TILE_LENGTH,
              /* layerId= */ 0),
          handler);
  private final CollidableTile upTile =
      new CollidableTile(
          Tile.create(
              FakeTiledMap.tiledMapWithDefaultPropertyValues(),
              EntityType.COLLIDABLE_TILE,
              /* tileId= */ 0,
              /* x= */ 1,
              /* y= */ 0,
              /* width= */ TILE_LENGTH,
              /* height= */ TILE_LENGTH,
              /* layerId= */ 0),
          handler);
  private final CollidableTile rightTile =
      new CollidableTile(
          Tile.create(
              FakeTiledMap.tiledMapWithDefaultPropertyValues(),
              EntityType.COLLIDABLE_TILE,
              /* tileId= */ 0,
              /* x= */ 2,
              /* y= */ 1,
              /* width= */ TILE_LENGTH,
              /* height= */ TILE_LENGTH,
              /* layerId= */ 0),
          handler);
  private final CollidableTile downTile =
      new CollidableTile(
          Tile.create(
              FakeTiledMap.tiledMapWithDefaultPropertyValues(),
              EntityType.COLLIDABLE_TILE,
              /* tileId= */ 0,
              /* x= */ 1,
              /* y= */ 2,
              /* width= */ TILE_LENGTH,
              /* height= */ TILE_LENGTH,
              /* layerId= */ 0),
          handler);

  @Test
  void handleCollision_shallowAxisIsVerticalAndSubjectIsBelow_movesSubjectDown() {
    handler.handleCollision(upTile, subject);

    assertThat(subject).isLocatedAt(80, 100);
  }

  @Test
  void handleCollision_shallowAxisIsVerticalAndSubjectIsAbove_movesSubjectUp() {
    handler.handleCollision(downTile, subject);

    assertThat(subject).isLocatedAt(80, 60);
  }

  @Test
  void handleCollision_shallowAxisIsHorizontalAndSubjectIsToTheRight_movesSubjectToTheRight() {
    handler.handleCollision(leftTile, subject);

    assertThat(subject).isLocatedAt(100, 80);
  }

  @Test
  void handleCollision_shallowAxisIsHorizontalAndSubjectIsToTheLeft_movesSubjectToTheLeft() {
    handler.handleCollision(rightTile, subject);

    assertThat(subject).isLocatedAt(60, 80);
  }

  @Test
  void handleCollision_subjectIsBelowAndMovingDownInVerticalCollision_doesNothing() {
    subject.setVerticalSpeed(1.2f);

    handler.handleCollision(upTile, subject);

    assertThat(subject).isLocatedAt(80, 80);
  }

  @Test
  void handleCollision_subjectIsAboveAndMovingUpInVerticalCollision_doesNothing() {
    subject.setVerticalSpeed(-1.2f);

    handler.handleCollision(downTile, subject);

    assertThat(subject).isLocatedAt(80, 80);
  }

  @Test
  void handleCollision_isVerticalCollisionAndSubjectIsAbove_setsStateToLanded() {
    handler.handleCollision(downTile, subject);

    assertThat(subject).isLanded();
  }
}
