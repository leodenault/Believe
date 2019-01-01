package believe.gamestate;

import static believe.util.MapEntry.entry;
import static believe.util.Util.hashMapOf;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import believe.core.io.FontLoader;
import believe.map.gui.MapManager;
import believe.physics.manager.PhysicsManager;
import believe.testing.FakeGameContainer;
import believe.util.MapEntry;
import believe.util.Util;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.state.StateBasedGame;

import java.awt.Font;
import java.util.Map;
import java.util.function.Function;

public class PlatformingStateTest {
  private PlatformingState state;
  private GameContainer container;

  @Mock private StateBasedGame game;
  @Mock private PhysicsManager physicsManager;
  @Mock private Function<PlatformingState, Void> singleEventAction;
  @Mock private FontLoader fontLoader;

  @Before
  public void setUp() throws SlickException {
    initMocks(this);

    when(fontLoader.getBaseFont()).thenReturn(new TrueTypeFont(new Font(
        "verdana",
        Font.PLAIN,
        20), /* antiAliased= */ true));

    container = new FakeGameContainer(game);
    state = new PlatformingState(container,
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
