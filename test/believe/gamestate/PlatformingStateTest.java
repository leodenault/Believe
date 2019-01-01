package believe.gamestate;

import static believe.util.MapEntry.entry;
import static believe.util.Util.hashMapOf;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import believe.core.io.FontLoader;
import believe.map.gui.MapManager;
import believe.physics.manager.PhysicsManager;
import believe.testing.FakeGameContainer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import java.util.function.Function;

public class PlatformingStateTest {
  private static final class FakeFontLoader extends FontLoader {
      private static final Font FONT = new Font() {
        @Override
        public int getWidth(String s) {
          return 0;
        }

        @Override
        public int getHeight(String s) {
          return 0;
        }

        @Override
        public int getLineHeight() {
          return 0;
        }

        @Override
        public void drawString(float v, float v1, String s) {

        }

        @Override
        public void drawString(float v, float v1, String s, Color color) {

        }

        @Override
        public void drawString(float v, float v1, String s, Color color, int i, int i1) {

        }
      };

    FakeFontLoader() {
      super(500, 500);
    }

    @Override
    public Font getBaseFont() {
      return FONT;
    }

    @Override
    public Font getBaseFontAtSize(float size) {
      return FONT;
    }
  }

  private PlatformingState state;
  private GameContainer container;

  @Mock private StateBasedGame game;
  @Mock private PhysicsManager physicsManager;
  @Mock private Function<PlatformingState, Void> singleEventAction;

  @Before
  public void setUp() throws SlickException {
    initMocks(this);

    container = new FakeGameContainer(game);
    FontLoader fontLoader = new FakeFontLoader();

    state = new PlatformingState(
        container,
        game,
        MapManager.defaultManager(),
        physicsManager,
        hashMapOf(entry(Input.KEY_LEFT, singleEventAction)),
        fontLoader);
  }

  @Test
  public void eventActionsAreUsed() {
    state.keyPressed(Input.KEY_LEFT, '\0');

    verify(singleEventAction).apply(state);
  }
}
