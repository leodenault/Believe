package musicGame.character;

import java.util.LinkedList;
import java.util.List;

import musicGame.core.SynchedComboPattern;
import musicGame.physics.Collidable;
import musicGame.physics.CommandCollidable;
import musicGame.physics.CommandCollisionHandler;
import musicGame.physics.DamageHandler.Faction;
import musicGame.statemachine.State.Action;

import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;

public class PlayableCharacter extends Character implements  CommandCollidable {
	public interface SynchedComboListener {
		void activateCombo(SynchedComboPattern pattern);
	}

	private boolean onRails;

	private SynchedComboPattern pattern;
	private List<SynchedComboListener> comboListeners;
	private CommandCollisionHandler commandHandler;

	public PlayableCharacter(GUIContext container, boolean onRails, int x, int y)
			throws SlickException {
		super(container, x, y);
		this.onRails = onRails;
		pattern = new SynchedComboPattern();
		pattern.addAction(0, 's');
		pattern.addAction(1, 's');
		pattern.addAction(2, 'a');
		pattern.addAction(2.5f, 'd');
		pattern.addAction(3, 'a');
		comboListeners = new LinkedList<SynchedComboListener>();
		commandHandler = new CommandCollisionHandler();
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
			Action action = null;
			switch (key) {
				case Input.KEY_LEFT:
					action = machine.getStates().contains(MOVING_RIGHT) ?
							Action.STOP : Action.SELECT_LEFT;
					break;
				case Input.KEY_RIGHT:
					action = machine.getStates().contains(MOVING_LEFT) ?
							Action.STOP : Action.SELECT_RIGHT;
					break;
				case Input.KEY_SPACE:
					action = Action.JUMP;
					break;
			}

			if (action != null) {
				machine.transition(action);
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
		if (!onRails) {
			Action action = null;
			switch (key) {
				case Input.KEY_LEFT:
					action = machine.getStates().contains(MOVING_LEFT) ?
							Action.STOP : Action.SELECT_RIGHT;
					break;
				case Input.KEY_RIGHT:
					action = machine.getStates().contains(MOVING_RIGHT) ?
							Action.STOP : Action.SELECT_LEFT;
					break;
			}

			if (action != null) {
				machine.transition(action);
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
