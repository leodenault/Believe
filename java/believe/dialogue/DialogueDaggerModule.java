package believe.dialogue;

import believe.character.playable.PlayableDaggerModule;
import believe.command.CommandSupplier;
import believe.datamodel.protodata.BinaryProtoFile;
import believe.datamodel.protodata.BinaryProtoFile.BinaryProtoFileFactory;
import believe.dialogue.InternalQualifiers.DialogueNameProperty;
import believe.dialogue.InternalQualifiers.FollowupCommandsProperty;
import believe.dialogue.InternalQualifiers.ModulePrivate;
import believe.dialogue.proto.DialogueProto.DialogueMap;
import believe.react.NotificationStrategy;
import believe.react.Observable;
import believe.react.ObservableValue;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import dagger.multibindings.StringKey;
import javax.inject.Singleton;

import java.util.Optional;
import java.util.function.Supplier;

/** Dagger module for bindings used by the dialogue package. */
@Module(includes = PlayableDaggerModule.class)
public abstract class DialogueDaggerModule {
  @Provides
  @Singleton
  static Supplier<DialogueMap> provideDialogueMap(BinaryProtoFileFactory binaryProtoFileFactory) {
    DialogueMap dialogueMap =
        binaryProtoFileFactory.create("res/dialogue/dialogue.pb", DialogueMap.parser()).load();
    return dialogueMap == null ? DialogueMap::getDefaultInstance : () -> dialogueMap;
  }

  @Binds
  @IntoMap
  @StringKey("dialogue")
  abstract CommandSupplier bindDialogueCommandSupplier(DialogueSupplier dialogueSupplier);

  @Provides
  @Singleton
  @ModulePrivate
  static ObservableValue<Optional<DialogueData>> provideObservableDialogueDataValue() {
    return ObservableValue.of(Optional.empty(), NotificationStrategy.ALWAYS_NOTIFY);
  }

  @Binds
  abstract Observable<Optional<DialogueData>> bindObservableDialogueData(
      @ModulePrivate ObservableValue<Optional<DialogueData>> observableDialogueDataValue);

  @Provides
  @DialogueNameProperty
  static String provideDialogueNameProperty() {
    return "dialogue_name";
  }

  @Provides
  @FollowupCommandsProperty
  static String provideFollowupCommandsProperty() {
    return "followup_commands";
  }
}
