package believe.character.playable;

import static believe.util.MapEntry.entry;
import static believe.util.Util.hashMapOf;
import static believe.util.Util.hashSetOf;
import static believe.util.Util.immutableMapOf;

import believe.character.Character;
import believe.character.Faction;
import believe.core.SynchedComboPattern;
import believe.core.display.SpriteSheetManager;
import believe.map.collidable.command.CommandCollidable;
import believe.physics.collision.Collidable;
import believe.physics.collision.CollisionHandler;
import believe.physics.manager.PhysicsManager;
import believe.statemachine.State;
import believe.statemachine.State.Action;
import believe.util.MapEntry;
import com.google.auto.factory.AutoFactory;
import com.google.auto.factory.Provided;
import javax.inject.Inject;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

@AutoFactory(allowSubclasses = true)
public class PlayableCharacter extends Character<PlayableCharacter>
    implements CommandCollidable<PlayableCharacter> {
  public interface SynchedComboListener {
    void activateCombo(SynchedComboPattern pattern);
  }

  private static final String SPRITE_SHEET_NAME = "stickFigure";
  private static final Map<Integer, Action> BASE_KEY_PRESSED_MAP =
      immutableMapOf(
          entry(Input.KEY_LEFT, Action.SELECT_LEFT),
          entry(Input.KEY_RIGHT, Action.SELECT_RIGHT),
          entry(Input.KEY_SPACE, Action.JUMP));

  private final Map<State, Set<MapEntry<Integer, Action>>> KEY_PRESSED_MODS =
      immutableMapOf(
          entry(
              standingState,
              hashSetOf(
                  entry(Input.KEY_LEFT, Action.SELECT_LEFT),
                  entry(Input.KEY_RIGHT, Action.SELECT_RIGHT))),
          entry(movingLeftState, hashSetOf(entry(Input.KEY_RIGHT, Action.STOP))),
          entry(movingRightState, hashSetOf(entry(Input.KEY_LEFT, Action.STOP))));
  private final Map<State, Set<MapEntry<Integer, Action>>> KEY_RELEASED_MODS =
      immutableMapOf(
          entry(
              standingState,
              hashSetOf(
                  entry(Input.KEY_LEFT, Action.SELECT_RIGHT),
                  entry(Input.KEY_RIGHT, Action.SELECT_LEFT))),
          entry(movingLeftState, hashSetOf(entry(Input.KEY_LEFT, Action.STOP))),
          entry(movingRightState, hashSetOf(entry(Input.KEY_RIGHT, Action.STOP))));
  private final DamageProjection damageProjection;

  private boolean onRails;
  private Map<Integer, Action> keyPressedActionMap;
  private Map<Integer, Action> keyReleasedActionMap;

  private SynchedComboPattern pattern;
  private List<SynchedComboListener> comboListeners;

  @Inject
  public PlayableCharacter(
      @Provided GUIContext container,
      @Provided PhysicsManager physicsManager,
      @Provided Set<CollisionHandler<? extends Collidable<?>, ? super PlayableCharacter>>
          rightCompatibleHandlers,
      DamageListener damageListener,
      boolean onRails,
      int x,
      int y) {
    super(container, damageListener, rightCompatibleHandlers, x, y);
    this.damageProjection =
        new DamageProjection(
            SpriteSheetManager.getInstance().getAnimationSet(SPRITE_SHEET_NAME).get("damage"),
            physicsManager,
            150);
    physicsManager.addGravityObject(damageProjection);
    this.onRails = onRails;
    pattern = new SynchedComboPattern();
    pattern.addAction(0, 's');
    pattern.addAction(1, 's');
    pattern.addAction(2, 'a');
    pattern.addAction(2.5f, 'd');
    pattern.addAction(3, 'a');
    comboListeners = new LinkedList<>();
    keyPressedActionMap = new HashMap<>(BASE_KEY_PRESSED_MAP);
    keyReleasedActionMap = hashMapOf();
  }

  @Override
  protected void renderComponent(GUIContext context, Graphics g) throws SlickException {
    if (!damageProjection.isStopped()) {
      damageProjection.render();
    }
    super.renderComponent(context, g);
  }

  public void addComboListener(SynchedComboListener listener) {
    this.comboListeners.add(listener);
  }

  @Override
  public void update(int delta) {
    super.update(delta);
    if (!damageProjection.isStopped()) {
      damageProjection.update(delta);
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
  public void inflictDamage(float damage, Faction inflictor) {
    super.inflictDamage(damage, inflictor);
    damageProjection.trigger(orientation, getX(), getY());
  }

  @Override
  protected String getSheetName() {
    return SPRITE_SHEET_NAME;
  }

  public Faction getFaction() {
    return Faction.GOOD;
  }
}
