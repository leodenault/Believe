package musicGame.map.gui;

import java.util.LinkedList;
import java.util.List;

import musicGame.map.collidable.Command;
import musicGame.map.io.MapProperties;
import musicGame.map.collidable.Tile;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.tiled.TiledMap;

import musicGame.character.Character;
import musicGame.character.playable.EnemyCharacter;
import musicGame.core.display.Camera.Layerable;
import musicGame.gui.ComponentBase;

public class LevelMap extends ComponentBase implements Layerable {
  public static final int TARGET_WIDTH = 1600;
  public static final int TARGET_HEIGHT = 900;

  private TiledMap map;
  private MapProperties properties;
  private ComponentBase focus;
  private List<EnemyCharacter> enemies;
  private List<MapBackground> backgrounds;

  public LevelMap(GUIContext container, String location, String tileSetLocation)
      throws SlickException {
    super(container, 0, 0);
    map = new TiledMap(location, tileSetLocation);
    properties = MapProperties.create(map, container);
    enemies = properties.enemies;
    rect.setSize(map.getWidth() * map.getTileWidth(), map.getHeight() * map.getTileHeight());
    backgrounds = new LinkedList<MapBackground>();
  }

  public void reset() {
    setLocation(0, 0);
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

  public List<Command> getCommands() {
    return properties.commands;
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

  public void addBackground(String image, int layer, int y) throws SlickException {
    this.backgrounds.add(new MapBackground(container, image, layer, y * map.getTileHeight()));
  }

  public List<MapBackground> getBackgrounds() {
    return backgrounds;
  }

  @Override
  public int getLayer() {
    return 0;
  }

  @Override
  public void resetLayout() {}

  @Override
  public void renderComponent(GUIContext context, Graphics g, float xMin, float xMax)
      throws SlickException {
    renderComponent(context, g);
  }

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
}
