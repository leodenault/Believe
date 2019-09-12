package believe.gamestate.levelstate.platformingstate;

import static believe.util.MapEntry.entry;
import static believe.util.Util.hashMapOf;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import believe.character.playable.PlayableCharacterFactory;
import believe.core.io.FontLoader;
import believe.core.io.testing.FakeFontLoader;
import believe.map.gui.LevelMapFactory;
import believe.map.gui.PlayAreaFactory;
import believe.map.io.MapManager;
import believe.physics.manager.PhysicsManager;
import believe.testing.FakeGameContainer;
import javax.inject.Provider;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.newdawn.slick.Color;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;

import java.util.function.Function;

public class PlatformingStateTest {
  private PlatformingState state;
  private GameContainer container;

  @Mock private StateBasedGame game;
  @Mock private PhysicsManager physicsManager;
  @Mock private Function<PlatformingState, Void> singleEventAction;
  @Mock private MapManager mapManager;
  @Mock private PlayAreaFactory playAreaFactory;
  @Mock private PlayableCharacterFactory playableCharacterFactory;

  @Before
  public void setUp() {
    initMocks(this);

    container = new FakeGameContainer(game);
    FontLoader fontLoader = new FakeFontLoader();

    state =
        new PlatformingState(
            container,
            game,
            mapManager,
            physicsManager,
            hashMapOf(entry(Input.KEY_LEFT, singleEventAction)),
            fontLoader,
            playAreaFactory,
            playableCharacterFactory);
  }

  @Test
  public void eventActionsAreUsed() {
    state.keyPressed(Input.KEY_LEFT, '\0');

    verify(singleEventAction).apply(state);
  }
}
