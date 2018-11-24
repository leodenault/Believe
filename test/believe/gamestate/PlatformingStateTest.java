package believe.gamestate;

import static believe.util.MapEntry.entry;
import static believe.util.Util.hashMapOf;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;

import believe.map.gui.MapManager;
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
import org.newdawn.slick.state.StateBasedGame;

import java.util.Map;
import java.util.function.Function;

public class PlatformingStateTest {
  private PlatformingState state;
  private GameContainer container;

  @Mock private StateBasedGame game;
  @Mock private Function<PlatformingState, Void> singleEventAction;

  @Before
  public void setUp() throws SlickException {
    initMocks(this);
    container = new FakeGameContainer(game);
    state = new PlatformingState(
        container,
        game,
        MapManager.defaultManager(),
        hashMapOf(entry(Input.KEY_LEFT, singleEventAction)));
  }

  @Test
  public void eventActionsAreUsed() {
    state.keyPressed(Input.KEY_LEFT, '\0');

    verify(singleEventAction).apply(state);
  }
}
