package believe.app.game;

import believe.app.AppUtil;
import believe.app.ApplicationModule;
import org.newdawn.slick.SlickException;

public class Believe {
  public static void main(String[] args)
      throws SlickException, NoSuchFieldException, IllegalAccessException {
    AppUtil.setNativesOnJavaLibraryPath();

    DaggerBelieveComponent.builder()
        .applicationModule(new ApplicationModule(args))
        .build()
        .gameContainer()
        .start();
  }
}
