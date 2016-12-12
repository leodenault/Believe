package musicGame.map;

import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.tiled.TiledMap;

import musicGame.character.PlayableCharacter.Action;
import musicGame.geometry.Rectangle;
import musicGame.map.MapProperties.MapEntityGenerator;
import musicGame.physics.Collidable;

public class Command implements Collidable {
	public static class Generator implements MapEntityGenerator<Command> {
		private static final String COMMAND_PROP = "command";
		private static final String INVALID = "invalid";
		@SuppressWarnings("serial")
		private static final Map<String, Action> PROP_ACTION_MAP = new HashMap<String, Action>() {{
			put("right", Action.SELECT_RIGHT);
			put("left", Action.SELECT_LEFT);
			put("jump", Action.JUMP);
			put("stop", Action.STOP);
		}};
		
		@Override
		public Command generateMapEntity(
				TiledMap map,
				GUIContext container,
				int tileId,
				int x,
				int y,
				int tileWidth,
				int tileHeight,
				int layer)
						throws SlickException {
			String property = map.getTileProperty(tileId, COMMAND_PROP, INVALID);
			if (!PROP_ACTION_MAP.containsKey(property)) {
				throw new RuntimeException(String.format("The property '%s' could not be read as a "
						+ "command. There must be an error with the commands tileset properties.",
						property));
			}
			return new Command(
					x * tileWidth,
					y * tileHeight,
					tileWidth,
					tileHeight,
					PROP_ACTION_MAP.get(property));
		}
	}

	private Action type;
	private Rectangle rect;
	
	public Command(int x, int y, int tileWidth, int tileHeight, Action type) {
		rect = new Rectangle(x, y, tileWidth, tileHeight);
		this.type = type;
	}
	
	public Action getCommandType() {
		return type;
	}

	@Override
	public void collision(Collidable other) {}

	@Override
	public CollidableType getType() {
		return CollidableType.COMMAND;
	}

	@Override
	public Rectangle getRect() {
		return rect;
	}
}
