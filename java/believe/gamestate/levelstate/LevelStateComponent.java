package believe.gamestate.levelstate;

import believe.core.io.FontLoader;
import believe.gamestate.GameStateRunner;
import believe.gamestate.StateController;
import believe.gui.GuiAction;
import believe.gui.GuiConfigurations;
import believe.gui.ImageSupplier;
import believe.input.InputAdapter;
import believe.io.ResourceManager;
import com.google.protobuf.ExtensionRegistry;
import dagger.BindsInstance;
import dagger.Component;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.gui.GUIContext;
import java.util.Set;

@Component(modules = LevelStateDaggerModule.class)
interface LevelStateComponent {
  LevelStateController getLevelStateController();

  @LevelStateRunner
  GameStateRunner getLevelStateRunner();

  @Component.Factory
  interface Factory {
    LevelStateComponent create(
        @BindsInstance @LevelName String levelName,
        @BindsInstance GUIContext guiContext,
        @BindsInstance GameContainer gameContainer,
        @BindsInstance StateController stateController,
        @BindsInstance ResourceManager resourceManager,
        @BindsInstance ExtensionRegistry extensionRegistry,
        @BindsInstance FontLoader fontLoader,
        @BindsInstance InputAdapter<GuiAction> inputAdapter,
        @BindsInstance ImageSupplier imageSupplier,
        @BindsInstance @GuiConfigurations Set<?> guiConfigurations);
  }
}
