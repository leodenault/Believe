package believe.gamestate.levelstate.arcadestate;

import believe.app.proto.GameOptionsProto.GameOptions;
import believe.character.Faction;
import believe.character.playable.PlayableCharacter;
import believe.character.playable.PlayableCharacterFactory;
import believe.command.Command;
import believe.core.io.FontLoader;
import believe.datamodel.MutableValue;
import believe.dialogue.DialogueData;
import believe.gamestate.levelstate.LevelState;
import believe.gui.CharacterDialogue;
import believe.gui.CharacterDialogue.DialogueResponse;
import believe.gui.CharacterDialogueFactory;
import believe.io.ResourceManager;
import believe.level.LevelManager;
import believe.levelFlow.component.FlowComponent;
import believe.levelFlow.component.FlowComponentListener;
import believe.levelFlow.parsing.FlowComponentBuilder;
import believe.levelFlow.parsing.FlowFileParser;
import believe.levelFlow.parsing.exceptions.FlowComponentBuilderException;
import believe.levelFlow.parsing.exceptions.FlowFileParserException;
import believe.map.data.MapData;
import believe.map.gui.PlayArea;
import believe.map.gui.PlayAreaFactory;
import believe.physics.manager.PhysicsManager;
import believe.react.Observable;
import believe.react.Observer;
import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.inject.Singleton;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.AbstractComponent;
import org.newdawn.slick.state.StateBasedGame;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Singleton
public class ArcadeState extends LevelState
    implements FlowComponentListener, Observer<Optional<DialogueData>> {
  private static final float FOCUS_RECHARGE_TIME = 45f; // Time in seconds for draining focus fully
  private static final float FOCUS_RECHARGE_RATE =
      PlayableCharacter.MAX_FOCUS / (FOCUS_RECHARGE_TIME * 1000f);
  private static final float HEALTH_PER_SUCCESS = 0.01f;
  private static final float DAMAGE_PER_FAILURE = 0.03f;

  private final PlayAreaFactory playAreaFactory;
  private final CharacterDialogueFactory characterDialogueFactory;

  private FlowComponent component;
  @Nullable private CharacterDialogue characterDialogue;

  @Inject
  public ArcadeState(
      GameContainer container,
      StateBasedGame game,
      PhysicsManager physicsManager,
      FontLoader fontLoader,
      Supplier<GameOptions> gameOptions,
      LevelManager levelManager,
      PlayAreaFactory playAreaFactory,
      PlayableCharacterFactory playableCharacterFactory,
      CharacterDialogueFactory characterDialogueFactory,
      Observable<Optional<DialogueData>> observableDialogueData,
      MutableValue<Optional<PlayableCharacter>> currentPlayableCharacter,
      ResourceManager resourceManager) {
    super(
        container,
        game,
        levelManager,
        physicsManager,
        fontLoader,
        playableCharacterFactory,
        currentPlayableCharacter);

    this.playAreaFactory = playAreaFactory;
    this.characterDialogueFactory = characterDialogueFactory;

    observableDialogueData.addObserver(this);

    FlowComponentBuilder builder =
        new FlowComponentBuilder(container, (int) (0.2 * container.getWidth()));
    try {
      new FlowFileParser(
              new InputStreamReader(resourceManager.getResourceAsStream("levelFlowFiles/Drive.lfl")),
              builder)
          .parse();
      component = builder.buildFlowComponent();
      component.setSpeedMultiplier(gameOptions.get().getGameplayOptions().getFlowSpeed());
      component.setLocation((int) (0.8 * container.getWidth()), 0);
      component.setHeight(container.getHeight());
      component.addListener(this);
    } catch (IOException
        | FlowFileParserException
        | FlowComponentBuilderException
        | SlickException e) {
      throw new RuntimeException(
          String.format(
              "Could not start arcade state because of the following exception:\n\n %s\n%s",
              e.getMessage(), e.getStackTrace()));
    }
  }

  @Override
  public void update(GameContainer container, StateBasedGame game, int delta)
      throws SlickException {
    super.update(container, game, delta);
    component.update(delta);
    if (!component.isPlaying()) {
      component.play();
    }
    getPlayer().inflictDamage(delta * FOCUS_RECHARGE_RATE, Faction.NONE);
  }

  @Override
  public void render(GameContainer container, StateBasedGame game, Graphics g)
      throws SlickException {
    super.render(container, game, g);
    component.render(container, g);

    if (characterDialogue != null) {
      characterDialogue.render(container, g);
    }
  }

  @Override
  public void keyPressed(int key, char c) {
    super.keyPressed(key, c);
    if (key == Input.KEY_ESCAPE) {
      this.component.pause();
    }

    if (key == Input.KEY_ENTER && characterDialogue != null) {
      if (!characterDialogue.scroll()) {
        characterDialogue.getFollupCommand().ifPresent(Command::execute);
        characterDialogue = null;
      }
    }
  }

  @Override
  public void reset() {
    super.reset();
    component.reset();
  }

  @Override
  public void levelEnter(GameContainer container, StateBasedGame game) {
    component.stop();
  }

  @Override
  protected boolean isOnRails() {
    return true;
  }

  @Override
  protected String getLevelName() {
    return "pipeTown";
  }

  @Override
  protected String getMusicLocation() {
    return "/res/music/Evasion.ogg";
  }

  @Override
  protected PlayArea providePlayArea(MapData mapData, PlayableCharacter player) {
    return playAreaFactory.create(mapData, player, 0.8f, 1f);
  }

  @Override
  public void componentActivated(AbstractComponent source) {}

  @Override
  public void beatSuccess(int index) {
    getPlayer().heal(HEALTH_PER_SUCCESS);
  }

  @Override
  public void beatFailed() {
    getPlayer().inflictDamage(DAMAGE_PER_FAILURE, Faction.NONE);
  }

  @Override
  public void beatMissed() {}

  @Override
  public void songEnded() {}

  @Override
  public void valueChanged(Optional<DialogueData> newValue) {
    if (!newValue.isPresent()) {
      characterDialogue = null;
      return;
    }

    characterDialogue =
        characterDialogueFactory.create(
            newValue.get().dialogue().getResponsesList().stream()
                .map(
                    response -> {
                      try {
                        return new DialogueResponse(new Image(0, 0), response.getResponseText());
                      } catch (SlickException e) {
                        throw new RuntimeException(e);
                      }
                    })
                .collect(Collectors.toList()),
            newValue.get().followupCommand());
  }
}
