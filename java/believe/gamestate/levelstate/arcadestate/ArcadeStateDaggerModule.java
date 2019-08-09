package believe.gamestate.levelstate.arcadestate;

import believe.datamodel.protodata.MutableProtoDataCommitter;
import believe.dialogue.proto.DialogueProto.DialogueMap;
import believe.map.collidable.command.CommandCollisionHandler;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import dagger.multibindings.StringKey;
import javax.inject.Singleton;

/** Dagger module for bindings used by the arcade state. */
@Module
public abstract class ArcadeStateDaggerModule {
  @Provides
  @Singleton
  static MutableProtoDataCommitter<DialogueMap> provideDialogueMapProtoDataCommitter() {
    return new MutableProtoDataCommitter<>(
        "believe/gamestate/levelstate/arcadestate/data/dialogue.pb",
        DialogueMap.parser(),
        DialogueMap.getDefaultInstance());
  }

  // @Provides
  // @IntoMap
  // @StringKey("dialogue")
  // static CommandCollisionHandler<?> provideDialogueCommandAction() {
  //   return (first, second) -> {};
  // }
}
