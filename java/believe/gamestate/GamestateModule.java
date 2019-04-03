package believe.gamestate;

import believe.app.StateInstantiator;
import dagger.Lazy;
import dagger.Module;
import dagger.Provides;
import javax.inject.Singleton;

/** Dagger module for gamestates. */
@Module
public abstract class GamestateModule {
  // We need states to be lazily injected because there's a dependency cycle between many
  // states and GameContainer/StateBasedGame instances. This should ideally be broken up.
  @Provides
  @OptionsMenuStateInstantiator
  static StateInstantiator provideOptionsMenuStateInstantiator(
      Lazy<OptionsMenuState> optionsMenuState) {
    return (container, game, fontLoader) -> optionsMenuState.get();
  }

  @Provides
  @ArcadeStateInstantiator
  static StateInstantiator provideArcadeStateInstantiator(Lazy<ArcadeState> arcadeState) {
    return (container, game, fontLoader) -> arcadeState.get();
  }

  @Provides
  @PlayFlowFileStateInstantiator
  static StateInstantiator providePlayFlowFileStateInstantiator(
      Lazy<PlayFlowFileState> playFlowFileState) {
    return (container, game, fontLoader) -> playFlowFileState.get();
  }

  @Provides
  @FlowFilePickerMenuStateInstantiator
  static StateInstantiator provideFlowFilePickerMenuStateInstantiator(
      Lazy<FlowFilePickerMenuState> flowFilePickerMenuState) {
    return (container, game, fontLoader) -> flowFilePickerMenuState.get();
  }
}
