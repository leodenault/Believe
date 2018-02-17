package believe.gamestate;

import java.io.IOException;

import believe.levelFlow.parsing.exceptions.FlowComponentBuilderException;
import believe.levelFlow.parsing.exceptions.FlowFileParserException;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;

public class ExternalLoadGameAction extends ChangeStateAction {
  public interface LoadableState extends GameState {
    void loadFile(String fileName)
      throws IOException, FlowFileParserException, SlickException, FlowComponentBuilderException;
  }

  private String flowFile;

  public ExternalLoadGameAction(Class<? extends LoadableState> state, String flowFile, StateBasedGame game) {
    super(state, game);
    this.flowFile = flowFile;
  }

  @Override
  public void componentActivated(AbstractComponent component) {
    try {
      ((LoadableState) GameStateBase.getStateInstance(state)).loadFile(this.flowFile);
    } catch (IOException | FlowFileParserException | SlickException
        | FlowComponentBuilderException e) {
      // TODO Properly handle the exception
      e.printStackTrace();
    }
    super.componentActivated(component);
  }

}
