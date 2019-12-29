package believe.gamestate.levelstate.platformingstate;

import static believe.util.MapEntry.entry;
import static believe.util.Util.hashMapOf;
import static org.mockito.Mockito.verify;
import static org.mockito.MockitoAnnotations.initMocks;

import believe.character.playable.PlayableCharacterFactory;
import believe.core.io.FontLoader;
import believe.core.io.testing.FakeFontLoader;
import believe.datamodel.MutableValue;
import believe.level.LevelManager;
import believe.map.gui.PlayAreaFactory;
import believe.physics.manager.PhysicsManager;
import believe.testing.FakeGameContainer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Input;
import org.newdawn.slick.state.StateBasedGame;

import java.util.Optional;
import java.util.function.Function;

public class PlatformingStateTest {
  private PlatformingState state;
  private GameContainer container;

  @Mock private StateBasedGame game;
  @Mock private PhysicsManager physicsManager;
  @Mock private Function<PlatformingState, Void> singleEventAction;
  @Mock private LevelManager levelManager;
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
            levelManager,
            physicsManager,
            hashMapOf(entry(Input.KEY_LEFT, singleEventAction)),
            fontLoader,
            playAreaFactory,
            playableCharacterFactory,
            MutableValue.of(Optional.empty()));
  }

  @Test
  public void eventActionsAreUsed() {
    state.keyPressed(Input.KEY_LEFT, '\0');

    verify(singleEventAction).apply(state);
  }
}
