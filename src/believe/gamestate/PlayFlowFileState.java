package believe.gamestate;

import believe.core.Options;
import believe.gamestate.ExternalLoadGameAction.LoadableState;
import believe.levelFlow.component.FlowComponent;
import believe.levelFlow.component.FlowComponentListener;
import believe.levelFlow.parsing.FlowComponentBuilder;
import believe.levelFlow.parsing.FlowFileParser;
import believe.levelFlow.parsing.exceptions.FlowComponentBuilderException;
import believe.levelFlow.parsing.exceptions.FlowFileParserException;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.util.ResourceLoader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class PlayFlowFileState extends GameStateBase
    implements FlowComponentListener, OverlayablePrecedingState, LoadableState {
  private final GameContainer gameContainer;
  private final ChangeToTemporaryStateAction<OverlayablePrecedingState> pauseAction;

  boolean enteringFromPauseMenu;
  private FlowComponent component;
  private StateBasedGame game;

  public PlayFlowFileState(GameContainer gameContainer, StateBasedGame game) {
    this.gameContainer = gameContainer;
    this.pauseAction = new ChangeToTemporaryStateAction<>(GamePausedOverlay.class, this, game);
    enteringFromPauseMenu = false;
    this.game = game;
  }

  @Override
  public void loadFile(String flowFile)
      throws IOException, FlowFileParserException, SlickException, FlowComponentBuilderException {
    InputStream stream = ResourceLoader.getResourceAsStream(flowFile);
    GameContainer container = game.getContainer();
    FlowComponentBuilder builder = new FlowComponentBuilder(container, container.getWidth() / 3);
    InputStreamReader reader = new InputStreamReader(stream);
    FlowFileParser parser = new FlowFileParser(reader, builder);
    parser.parse();
    this.component = builder.buildFlowComponent();
    this.component.setSpeedMultiplier(Options.getInstance().flowSpeed);
    this.component.setLocation((container.getWidth() - component.getWidth()) / 2,
        container.getHeight() / 15);
    this.component.setHeight(container.getHeight());
    this.component.addListener(this);
  }

  @Override
  public void reset() {
    this.component.reset();
  }

  @Override
  public void keyPressed(int key, char c) {
    super.keyPressed(key, c);
    if (key == Input.KEY_ESCAPE) {
      component.pause();
      pauseAction.activate();
    }
  }

  @Override
  public void init(GameContainer container, StateBasedGame game) throws SlickException {
  }

  @Override
  public void render(GameContainer container, StateBasedGame game, Graphics g)
      throws SlickException {
    if (component != null) {
      component.render(container, g);
    }
  }

  @Override
  public void update(GameContainer container, StateBasedGame game, int delta)
      throws SlickException {
    if (component != null) {
      component.update(delta);
    }

    if (!component.isPlaying()) {
      component.play();
    }
  }

  @Override
  public void enter(GameContainer container, StateBasedGame game) throws SlickException {
    super.enter(container, game);
    if (component == null) {
      throw new RuntimeException("The file was not loaded prior to activating the flow component.");
    }
  }

  @Override
  public void leave(GameContainer container, StateBasedGame game) throws SlickException {
    super.leave(container, game);
    this.component.pause();
  }

  @Override
  public void beatSuccess(int index) {}

  @Override
  public void beatFailed() {}

  @Override
  public void beatMissed() {}

  @Override
  public void songEnded() {
    new ChangeStateAction<>(MainMenuState.class, game).componentActivated(null);
  }

  @Override
  public void componentActivated(AbstractComponent source) {}

  @Override
  public void exitingFollowingState() {
    this.enteringFromPauseMenu = false;
  }

  @Override
  public Image getCurrentScreenshot() throws SlickException {
    Image screenshot = new Image(gameContainer.getWidth(), gameContainer.getHeight());
    gameContainer.getGraphics().copyArea(screenshot, 0, 0);
    return screenshot;
  }
}
