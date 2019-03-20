package believe.app.game;

import believe.app.AppUtil;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.util.Log;

import java.util.Optional;

public class Believe {
  public static void main(String[] args)
      throws SlickException, NoSuchFieldException, IllegalAccessException {
    AppUtil.setNativesOnJavaLibraryPath();

    Optional<AppGameContainer>
        gameContainer =
        DaggerBelieveComponent
            .builder()
            .believeGameModule(new BelieveGameModule(args))
            .build()
            .gameContainer();

    if (!gameContainer.isPresent()) {
      Log.warn("No AppGameContainer instance is available. Exiting.");
      return;
    }
    gameContainer.get().start();
  }
}
