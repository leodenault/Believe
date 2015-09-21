package musicGame.map;

import java.util.LinkedList;
import java.util.List;

import musicGame.character.Character;
import musicGame.character.EnemyCharacter;
import musicGame.geometry.Rectangle;
import musicGame.gui.ComponentBase;

import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.tiled.TiledMap;
import org.newdawn.slick.util.Log;

public class LevelMap extends ComponentBase {
	private static final String MAP_DIRECTORY = "/res/maps";
	private static final String MAP_SUFFIX = ".tmx";
	
	public static final int TARGET_WIDTH = 1600;
	public static final int TARGET_HEIGHT = 900;
	
	private TiledMap map;
	private MapProperties properties;
	private ComponentBase focus;
	private List<EnemyCharacter> enemies;
			
	public LevelMap(GUIContext container, String name) throws SlickException {
		super(container, 0, 0);
		map = new TiledMap(String.format("%s/%s%s", MAP_DIRECTORY, name, MAP_SUFFIX), MAP_DIRECTORY);
		properties = MapProperties.create(map);
		enemies = generateEnemies(properties.enemies);
		rect.setSize(map.getWidth() * map.getTileWidth(), map.getHeight() * map.getTileHeight());
	}
	
	public void reset() {
		setLocation(0, 0);
		// TODO: Find a way to avoid this block
		try {
			enemies = generateEnemies(properties.enemies);
		} catch (SlickException e) {
			Log.error("There was an error re-generating enemies");
		}
	}
	
	public int getPlayerStartX() {
		return properties.startX * map.getTileWidth();
	}
	
	public int getPlayerStartY() {
		return (properties.startY + 1) * map.getTileHeight();
	}
	
	public List<Tile> getCollidableTiles() {
		return properties.collidableTiles;
	}
	
	public List<EnemyCharacter> getEnemies() {
		return enemies;
	}
	
	/**
	 * Sets the object that should be centered on screen, like the player
	 * 
	 * @param focus The component to focus on
	 */
	public void setFocus(ComponentBase focus) {
		this.focus = focus;
	}
	
	public void update(int delta) {
		for (EnemyCharacter enemy : enemies) {
			enemy.update(delta);
		}
	}

	@Override
	protected void resetLayout() {}

	@Override
	protected void renderComponent(GUIContext context, Graphics g)
			throws SlickException {
		for (Integer layer : properties.rearLayers) {
			map.render(getX(), getY(), layer);
		}
		
		for (Character enemy : enemies) {
			enemy.render(context, g);
		}
		
		if (focus != null) {
			focus.render(context, g);
		}
		
		for (Integer layer : properties.frontLayers) {
			map.render(getX(), getY(), layer);
		}
	}
	
	private List<EnemyCharacter> generateEnemies(List<Tile> enemyTiles) throws SlickException {
		List<EnemyCharacter> enemies = new LinkedList<EnemyCharacter>();
		
		for (Tile enemyTile : enemyTiles) {
			Rectangle rect = enemyTile.getRect();
			enemies.add(new EnemyCharacter(container, (int)rect.getX(), (int)rect.getMaxY()));
		}
		
		return enemies;
	}
}
