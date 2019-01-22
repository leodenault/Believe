package believe.app.flag_parsers;

public interface FlagParser<T> {
  T parseFlag(String flag);
}
