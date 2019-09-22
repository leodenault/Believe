package believe.dialogue;

import believe.character.playable.PlayableCharacter;
import believe.character.playable.PlayableDaggerModule;
import believe.datamodel.protodata.MutableProtoDataCommitter;
import believe.dialogue.InternalQualifiers.DialogueNameProperty;
import believe.dialogue.InternalQualifiers.ModulePrivate;
import believe.dialogue.proto.DialogueProto.Dialogue;
import believe.dialogue.proto.DialogueProto.DialogueMap;
import believe.map.collidable.command.CommandSupplier;
import believe.physics.collision.Collidable;
import believe.physics.collision.CollisionHandler;
import believe.react.Observable;
import believe.react.ObservableValue;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.multibindings.IntoMap;
import dagger.multibindings.IntoSet;
import dagger.multibindings.StringKey;
import javax.inject.Singleton;

import java.util.Optional;
import java.util.function.Supplier;

/** Dagger module for bindings used by the dialogue package. */
@Module(includes = PlayableDaggerModule.class)
public abstract class DialogueDaggerModule {
  @Provides
  @Singleton
  static MutableProtoDataCommitter<DialogueMap> provideDialogueMapProtoDataCommitter() {
    MutableProtoDataCommitter<DialogueMap> dialogueMap =
        new MutableProtoDataCommitter<>(
            "res/dialogue/dialogue.pb", DialogueMap.parser(), DialogueMap.getDefaultInstance());
    dialogueMap.load();
    return dialogueMap;
  }

  @Binds
  abstract Supplier<DialogueMap> bindDialogueMapSupplier(
      MutableProtoDataCommitter<DialogueMap> dialogueMapMutableProtoDataCommitter);

  @Binds
  @IntoSet
  abstract CollisionHandler<? extends Collidable<?>, ? super PlayableCharacter>
      bindDialogueCommandCollisionHandler(
          DialogueCommandCollisionHandler dialogueCommandCollisionHandler);

  @Binds
  @IntoMap
  @StringKey("dialogue")
  abstract CommandSupplier<PlayableCharacter, ?> bindDialogueCommandSupplier(
      DialogueSupplier dialogueSupplier);

  @Provides
  @Singleton
  @ModulePrivate
  static ObservableValue<Optional<Dialogue>> provideObservableDialogueValue() {
    return new ObservableValue<>(Optional.empty());
  }

  @Binds
  abstract Observable<Optional<Dialogue>> bindObservableDialogue(
      @ModulePrivate ObservableValue<Optional<Dialogue>> observableDialogueValue);

  @Provides
  @DialogueNameProperty
  static String provideDialogueNameProperty() {
    return "dialogue_name";
  }
}
