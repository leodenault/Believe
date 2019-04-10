package believe.app.game;

import believe.app.AppUtil;
import org.newdawn.slick.SlickException;

public class Believe {
  public static void main(String[] args)
      throws SlickException, NoSuchFieldException, IllegalAccessException {
    AppUtil.startApplication(args, DaggerBelieveComponent.create());
  }
}
