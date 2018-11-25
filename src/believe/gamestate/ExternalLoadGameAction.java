package believe.gamestate;

import java.io.IOException;

import believe.gamestate.ExternalLoadGameAction.LoadableState;
import believe.levelFlow.parsing.exceptions.FlowComponentBuilderException;
import believe.levelFlow.parsing.exceptions.FlowFileParserException;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.state.GameState;
import org.newdawn.slick.state.StateBasedGame;

public class ExternalLoadGameAction<StateT extends LoadableState> extends ChangeStateAction<StateT> {
  public interface LoadableState extends GameState {
    void loadFile(String fileName)
      throws IOException, FlowFileParserException, SlickException, FlowComponentBuilderException;
  }

  private String flowFile;

  public ExternalLoadGameAction(Class<StateT> state, String flowFile, StateBasedGame game) {
    super(state, game);
    this.flowFile = flowFile;
  }

  @Override
  public void componentActivated(AbstractComponent component) {
    try {
      GameStateBase.getStateInstance(state).loadFile(this.flowFile);
    } catch (IOException | FlowFileParserException | SlickException
        | FlowComponentBuilderException e) {
      // TODO Properly handle the exception
      e.printStackTrace();
    }
    super.componentActivated(component);
  }

}
