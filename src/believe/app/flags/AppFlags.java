package believe.app.flags;

import believe.app.flag_parsers.Flag;

public interface AppFlags {
  @Flag.Integer(name = "width", defaultValue = -1)
  int width();

  @Flag.Integer(name = "height", defaultValue = -1)
  int height();

  @Flag.Boolean(name = "windowed")
  boolean windowed();
}
