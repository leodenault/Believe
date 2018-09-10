package believe.character.playable;

import believe.character.Character;
import believe.character.Faction;
import believe.core.SynchedComboPattern;
import believe.physics.collision.Collidable;
import believe.physics.collision.CommandCollidable;
import believe.physics.collision.CommandCollisionHandler;
import believe.statemachine.State;
import believe.statemachine.State.Action;
import believe.util.MapEntry;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;

import java.util.*;

import static believe.util.MapEntry.entry;
import static believe.util.Util.*;

public class PlayableCharacter extends Character implements CommandCollidable {
  public interface SynchedComboListener {
    void activateCombo(SynchedComboPattern pattern);
  }

  private static final Map<Integer, Action> BASE_KEY_PRESSED_MAP =
      immutableMapOf(
          entry(Input.KEY_LEFT, Action.SELECT_LEFT),
          entry(Input.KEY_RIGHT, Action.SELECT_RIGHT),
          entry(Input.KEY_SPACE, Action.JUMP));
  private static final Map<State, Set<MapEntry<Integer, Action>>> KEY_PRESSED_MODS =
      immutableMapOf(
          entry(
              STANDING,
              hashSetOf(
                  entry(Input.KEY_LEFT, Action.SELECT_LEFT),
                  entry(Input.KEY_RIGHT, Action.SELECT_RIGHT))),
          entry(
              MOVING_LEFT,
              hashSetOf(
                  entry(Input.KEY_RIGHT, Action.STOP))),
          entry(
              MOVING_RIGHT,
              hashSetOf(
                  entry(Input.KEY_LEFT, Action.STOP))));
  private static final Map<State, Set<MapEntry<Integer, Action>>> KEY_RELEASED_MODS =
      immutableMapOf(
          entry(
              STANDING,
              hashSetOf(
                  entry(Input.KEY_LEFT, Action.SELECT_RIGHT),
                  entry(Input.KEY_RIGHT, Action.SELECT_LEFT))),
          entry(
              MOVING_LEFT,
              hashSetOf(
                  entry(Input.KEY_LEFT, Action.STOP))),
          entry(
              MOVING_RIGHT,
              hashSetOf(
                  entry(Input.KEY_RIGHT, Action.STOP))));

  private boolean onRails;
  private Map<Integer, Action> keyPressedActionMap;
  private Map<Integer, Action> keyReleasedActionMap;

  private SynchedComboPattern pattern;
  private List<SynchedComboListener> comboListeners;
  private CommandCollisionHandler commandHandler;

  public PlayableCharacter(
      GUIContext container,
      DamageListener damageListener,
      boolean onRails,
      int x,
      int y)
      throws SlickException {
    super(container, damageListener, x, y);
    this.onRails = onRails;
    pattern = new SynchedComboPattern();
    pattern.addAction(0, 's');
    pattern.addAction(1, 's');
    pattern.addAction(2, 'a');
    pattern.addAction(2.5f, 'd');
    pattern.addAction(3, 'a');
    comboListeners = new LinkedList<SynchedComboListener>();
    commandHandler = new CommandCollisionHandler();
    keyPressedActionMap = new HashMap<Integer, Action>(BASE_KEY_PRESSED_MAP);
    keyReleasedActionMap = hashMapOf();
  }

  public void addComboListener(SynchedComboListener listener) {
    this.comboListeners.add(listener);
  }

  @Override
  public void collision(Collidable other) {
    super.collision(other);
    if (onRails) {
      commandHandler.handleCollision(this, other);
    }
  }

  @Override
  public void keyPressed(int key, char c) {
    super.keyPressed(key, c);
    if (!onRails) {
      if (keyPressedActionMap.containsKey(key)) {
        machine.transition(keyPressedActionMap.get(key));
      }

      if (key == Input.KEY_C) {
        for (SynchedComboListener listener : comboListeners) {
          listener.activateCombo(pattern);
        }
      }
    }
  }

  @Override
  public void keyReleased(int key, char c) {
    super.keyReleased(key, c);
    if (!onRails && keyReleasedActionMap.containsKey(key)) {
      machine.transition(keyReleasedActionMap.get(key));
    }
  }

  @Override
  public void transitionEnded(Set<State> currentStates) {
    super.transitionEnded(currentStates);
    remapKeys(currentStates, KEY_PRESSED_MODS, keyPressedActionMap);
    remapKeys(currentStates, KEY_RELEASED_MODS, keyReleasedActionMap);
  }

  private void remapKeys(
      Set<State> states,
      Map<State, Set<MapEntry<Integer, Action>>> mods,
      Map<Integer, Action> keyActionMap) {
    for (State state : states) {
      if (mods.containsKey(state)) {
        for (MapEntry<Integer, Action> entry : mods.get(state)) {
          keyActionMap.put(entry.getKey(), entry.getValue());
        }
      }
    }
  }

  @Override
  protected String getSheetName() {
    return "stickFigure";
  }

  public Faction getFaction() {
    return Faction.GOOD;
  }

  @Override
  public void executeCommand(Action command) {
    machine.transition(command);
  }
}
