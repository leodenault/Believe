package believe.app.editor;

import believe.app.AppUtil;
import org.newdawn.slick.SlickException;

/** Entry point for the Believe Level Editor. */
public final class LevelEditor {
  public static void main(String[] args)
      throws IllegalAccessException, NoSuchFieldException, SlickException {
    AppUtil.startApplication(args, DaggerLevelEditorComponent.create());
  }
}
