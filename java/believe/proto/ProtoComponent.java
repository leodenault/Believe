package believe.proto;

import dagger.Component;

/** A Dagger component providing Google protobuf constructs. */
@Component(modules = ProtoDaggerModule.class)
public interface ProtoComponent {
  TextProtoParserFactory getTextProtoParserFactory();
}
