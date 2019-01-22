package believe.app.flag_parsers;

public class BooleanFlagParser implements FlagParser<Boolean> {

  @Override
  public Boolean parseFlag(String flag) {
    return true;
  }
}
