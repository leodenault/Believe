package believe.app.game;

import believe.app.AppGameContainerSupplier;
import believe.app.AppUtil;
import org.newdawn.slick.SlickException;

public class Believe {
  public static void main(String[] args)
      throws SlickException, NoSuchFieldException, IllegalAccessException {
    AppUtil.setNativesOnJavaLibraryPath();
    BelieveComponent component = DaggerBelieveComponent.create();
    AppGameContainerSupplier appGameContainerSupplier = component.appGameContainerSupplier();
    appGameContainerSupplier.initAppGameContainer(args, component.game());
    appGameContainerSupplier.get().start();
  }
}
