package believe.app;

import believe.animation.AnimationDaggerModule;
import believe.app.flag_parsers.CommandLineParser;
import believe.app.flags.AppFlags;
import believe.app.proto.GameOptionsProto.GameOptions;
import believe.character.CharacterDaggerModule;
import believe.command.CommandDaggerModule;
import believe.core.io.FontLoader;
import believe.core.io.JarClasspathLocation;
import believe.datamodel.DataCommitter;
import believe.datamodel.MutableValue;
import believe.datamodel.protodata.BinaryProtoFile;
import believe.datamodel.protodata.BinaryProtoFile.BinaryProtoFileFactory;
import believe.dialogue.DialogueDaggerModule;
import believe.gamestate.GamestateDaggerModule;
import believe.gamestate.levelstate.LevelStateDaggerModule;
import believe.gamestate.levelstate.platformingstate.EventActions;
import believe.gamestate.levelstate.platformingstate.PlatformingState;
import believe.gui.GuiDaggerModule;
import believe.gui.GuiDaggerModuleV2;
import believe.io.IoDaggerModule;
import believe.level.LevelDaggerModule;
import believe.map.collidable.command.CollidableCommandDaggerModule;
import believe.map.collidable.tile.CollidableTileDaggerModule;
import believe.map.io.MapParsingDaggerModule;
import believe.map.tiled.command.TiledCommandDaggerModule;
import believe.mob.MobDaggerModule;
import believe.physics.collision.CollisionDaggerModule;
import believe.proto.ProtoDaggerModule;
import believe.util.Util;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.Reusable;
import dagger.multibindings.IntoSet;
import dagger.multibindings.Multibinds;
import javax.inject.Singleton;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.gui.GUIContext;
import org.newdawn.slick.util.ClasspathLocation;
import org.newdawn.slick.util.FileSystemLocation;
import org.newdawn.slick.util.ResourceLocation;

import java.io.File;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

/** Dagger module used in all application components. */
@Module(
    includes = {
      AnimationDaggerModule.class,
      CharacterDaggerModule.class,
      CollidableTileDaggerModule.class,
      CollisionDaggerModule.class,
      CollidableCommandDaggerModule.class,
      CommandDaggerModule.class,
      DialogueDaggerModule.class,
      GamestateDaggerModule.class,
      GuiDaggerModule.class,
      GuiDaggerModuleV2.class,
      IoDaggerModule.class,
      LevelDaggerModule.class,
      LevelStateDaggerModule.class,
      MapParsingDaggerModule.class,
      MobDaggerModule.class,
      ProtoDaggerModule.class,
      TiledCommandDaggerModule.class
    })
public abstract class ApplicationDaggerModuleV2 {
  private static final String GAME_OPTIONS_FILE_NAME = "game_options.pb";

  @Provides
  @Singleton
  static BinaryProtoFile<GameOptions> provideGameOptionsFile(
      BinaryProtoFileFactory binaryProtoFileFactory) {
    return binaryProtoFileFactory.create(GAME_OPTIONS_FILE_NAME, GameOptions.parser());
  }

  @Provides
  @Singleton
  static MutableValue<GameOptions> provideGameOptions(
      BinaryProtoFile<GameOptions> gameOptionsFile) {
    return MutableValue.of(gameOptionsFile.load());
  }

  @Binds
  abstract Supplier<GameOptions> bindGameOptionsSupplier(MutableValue<GameOptions> gameOptions);

  @Binds
  abstract DataCommitter<GameOptions> bindGameOptionsDataCommitter(
      BinaryProtoFile<GameOptions> gameOptionsFile);

  @Multibinds
  @EventActions
  abstract Map<Integer, Function<PlatformingState, Void>> bindEventActionMap();

  @Binds
  abstract GUIContext bindGuiContext(GameContainer gameContainer);

  @Provides
  @Reusable
  static FontLoader provideFontLoader(GameContainer gameContainer) {
    FontLoader fontLoader = new FontLoader(gameContainer.getWidth(), gameContainer.getHeight());
    fontLoader.load();
    return fontLoader;
  }

  @Provides
  @Reusable
  static Font provideFont(FontLoader fontLoader) {
    return fontLoader.getBaseFont();
  }

  @Provides
  @IntoSet
  static ResourceLocation provideClasspathResourceLocation() {
    return new ClasspathLocation();
  }

  @Binds
  @IntoSet
  abstract ResourceLocation bindJarClasspathLocation(JarClasspathLocation jarClasspathLocation);

  @Provides
  static FileSystemLocation provideFileSystemLocation() {
    return new FileSystemLocation(new File("."));
  }

  @Provides
  @NativeLibraryPaths
  static Set<String> provideNativeLibraryPaths() {
    return Util.hashSetOf("third_party/jinput", "third_party/lwjgl", "third_party/openal");
  }

  @Provides
  @Reusable
  static Class<AppFlags> provideFlagsClass() {
    return AppFlags.class;
  }

  @Provides
  @Reusable
  static AppFlags provideCommandLineParser(CommandLineParser<AppFlags> commandLineParser) {
    return commandLineParser.parse();
  }

  @Provides
  @Singleton
  static AppGameContainer provideAppGameContainer(AppGameContainerFactory factory) {
    return factory.create();
  }

  @Binds
  abstract GameContainer bindGameContainer(AppGameContainer gameContainer);
}
