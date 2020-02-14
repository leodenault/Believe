package believe.proto;

import com.google.protobuf.ExtensionRegistry;
import com.google.protobuf.GeneratedMessage.GeneratedExtension;
import dagger.Binds;
import dagger.Module;
import dagger.Provides;
import dagger.Reusable;
import dagger.multibindings.Multibinds;

import java.util.Set;

@Module
public abstract class ProtoDaggerModule {
  @Multibinds
  abstract Set<GeneratedExtension<?, ?>> bindProtoExtensions();

  @Provides
  @Reusable
  static ExtensionRegistry provideExtensionRegistry(Set<GeneratedExtension<?, ?>> protoExtensions) {
    ExtensionRegistry extensionRegistry = ExtensionRegistry.newInstance();
    for (GeneratedExtension<?, ?> protoExtension : protoExtensions) {
      extensionRegistry.add(protoExtension);
    }
    return extensionRegistry;
  }

  @Binds
  abstract ProtoParser bindProtoParser(ProtoParserImpl impl);
}
