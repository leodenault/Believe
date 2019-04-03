package believe.gamestate;

import believe.app.StateInstantiator;
import believe.app.game.GameStateInstantiators;
import dagger.Lazy;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoSet;

/** Dagger module for gamestates. */
@Module
public abstract class GamestateModule {
  // We need states to be lazily injected because there's a dependency cycle between many
  // states and GameContainer/StateBasedGame instances. This should ideally be broken up.
  @Provides
  @IntoSet
  @GameStateInstantiators
  static StateInstantiator provideOptionsMenuStateInstantiator(
      Lazy<OptionsMenuState> optionsMenuState) {
    return (container, game, fontLoader) -> optionsMenuState.get();
  }

  @Provides
  @IntoSet
  @GameStateInstantiators
  static StateInstantiator provideArcadeStateInstantiator(Lazy<ArcadeState> arcadeState) {
    return (container, game, fontLoader) -> arcadeState.get();
  }

  @Provides
  @IntoSet
  @GameStateInstantiators
  static StateInstantiator providePlayFlowFileStateInstantiator(
      Lazy<PlayFlowFileState> playFlowFileState) {
    return (container, game, fontLoader) -> playFlowFileState.get();
  }

  @Provides
  @IntoSet
  @GameStateInstantiators
  static StateInstantiator provideFlowFilePickerMenuStateInstantiator(
      Lazy<FlowFilePickerMenuState> flowFilePickerMenuState) {
    return (container, game, fontLoader) -> flowFilePickerMenuState.get();
  }
}
