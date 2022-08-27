package believe.gamestate.levelstate;

import believe.audio.Music;
import believe.character.CharacterV2;
import believe.core.io.FontLoader;
import believe.gamestate.GameStateRunner;
import believe.gamestate.StateController;
import believe.gui.GuiAction;
import believe.gui.GuiConfigurations;
import believe.gui.ImageSupplier;
import believe.input.InputAdapter;
import believe.io.ResourceManager;
import believe.physics.manager.PhysicsManager;
import believe.scene.Camera;
import believe.scene.LevelMap;
import com.google.protobuf.ExtensionRegistry;
import dagger.BindsInstance;
import dagger.Component;
import java.util.Set;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.gui.GUIContext;

@Component(modules = LevelStateDaggerModule.class)
interface LevelStateComponent {
  LevelStateController getLevelStateController();

  @LevelStateRunner
  GameStateRunner getLevelStateRunner();

  @Component.Factory
  interface Factory {
    LevelStateComponent create(
        @BindsInstance GUIContext guiContext,
        @BindsInstance GameContainer gameContainer,
        @BindsInstance StateController stateController,
        @BindsInstance ResourceManager resourceManager,
        @BindsInstance ExtensionRegistry extensionRegistry,
        @BindsInstance FontLoader fontLoader,
        @BindsInstance InputAdapter<GuiAction> inputAdapter,
        @BindsInstance ImageSupplier imageSupplier,
        @BindsInstance @GuiConfigurations Set<?> guiConfigurations,
        @BindsInstance LevelMap levelMap,
        @BindsInstance Camera camera,
        @BindsInstance CharacterV2 player,
        @BindsInstance Music backgroundMusic,
        @BindsInstance PhysicsManager physicsManager);
  }
}
