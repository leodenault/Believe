package believe.tools;

import dagger.Component;

@Component(modules = ProtoFileSerializerDaggerModule.class)
public interface ProtoFileSerializerComponent {
  ProtoFileSerializer getProtoFileSerializer();
}
