package believe.proto;

import com.google.protobuf.ExtensionRegistry;
import dagger.Module;
import dagger.Provides;

@Module
abstract class ProtoDaggerModule {
  @Provides
  static ExtensionRegistry provideExtensionRegistry() {
    return ExtensionRegistry.newInstance();
  }
}
