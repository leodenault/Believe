package believe.app.flag_parsers;

public class IntegerFlagParser implements FlagParser<Integer> {

  @Override
  public Integer parseFlag(String flag) {
    return Integer.parseInt(flag);
  }
}
