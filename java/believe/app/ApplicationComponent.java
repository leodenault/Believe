package believe.app;

import org.newdawn.slick.state.StateBasedGame;

/** A Dagger component that allows building and running a game based on Slick 2D. */
public interface ApplicationComponent {
  /** The {@link StateBasedGame} that will be run as part of the application. */
  StateBasedGame game();

  /**
   * The {@link AppGameContainerSupplier} that will be used to initialize the {@link
   * org.newdawn.slick.AppGameContainer}.
   */
  AppGameContainerSupplier appGameContainerSupplier();
}
