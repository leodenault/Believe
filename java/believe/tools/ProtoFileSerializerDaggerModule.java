package believe.tools;

import believe.character.playable.PlayableDaggerModule;
import believe.command.CommandDaggerModule;
import believe.dialogue.DialogueDaggerModule;
import believe.proto.ProtoDaggerModule;
import dagger.Module;

@Module(
    includes = {
      CommandDaggerModule.class,
      DialogueDaggerModule.class,
      PlayableDaggerModule.class,
      ProtoDaggerModule.class
    })
abstract class ProtoFileSerializerDaggerModule {}
