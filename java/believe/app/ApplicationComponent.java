package believe.app;

import org.newdawn.slick.AppGameContainer;

import java.util.Optional;

/** A Dagger component that allows building and running a game based on Slick 2D. */
public interface ApplicationComponent {
  /**
   * The {@link AppGameContainer} used to begin running the game. Call {@link
   * AppGameContainer#start()} to begin running the game.
   */
  AppGameContainer gameContainer();
}
