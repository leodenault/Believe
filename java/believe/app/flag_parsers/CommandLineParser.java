package believe.app.flag_parsers;

import dagger.Reusable;
import javax.inject.Inject;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UnknownFormatFlagsException;

@Reusable
public final class CommandLineParser<T> {
  private final Class<T> flagsClass;
  private final String[] args;

  @Inject
  public CommandLineParser(Class<T> flagsClass, @CommandLineArguments String[] args) {
    this.flagsClass = flagsClass;
    this.args = args;
  }

  public T parse() {
    if (!flagsClass.isInterface()) {
      throw new IllegalArgumentException(
          "CommandLineParser requires interfaces with flag definitions.");
    }

    Map<String, MethodAndParser> flagMethods = new HashMap<>();
    Map<Method, Object> returns = new HashMap<>();

    // Run through the flag interface and find all of the relevant annotated methods.
    for (Method method : flagsClass.getDeclaredMethods()) {
      for (Annotation annotation : method.getAnnotations()) {
        Class<? extends Annotation> annotationType = annotation.annotationType();
        String name = "";
        FlagParser<?> parser = null;
        Object value = null;
        if (annotationType.equals(Flag.Integer.class)) {
          if (!method.getReturnType().equals(int.class)) {
            throw new IllegalStateException(
                String.format(
                    "Integer flag for method '%s' should " + "have integer return type.",
                    method.getName()));
          }
          Flag.Integer intAnnotation = (Flag.Integer) annotation;
          name = intAnnotation.name();
          parser = new IntegerFlagParser();
          value = intAnnotation.defaultValue();
        } else if (annotationType.equals(Flag.Boolean.class)) {
          if (!method.getReturnType().equals(boolean.class)) {
            throw new IllegalStateException(
                String.format(
                    "Boolean flag for method '%s' should " + "have boolean return type.",
                    method.getName()));
          }
          Flag.Boolean boolAnnotation = (Flag.Boolean) annotation;
          name = boolAnnotation.name();
          parser = new BooleanFlagParser();
          value = boolAnnotation.defaultValue();
        }

        // We found a parser whose flag name is empty. Throw an exception for this.
        if (name.equals("")) {
          if (parser != null) {
            throw new IllegalStateException(
                "Flag parsers must have a non-empty name for the command line flags they "
                    + "represent.");
          }
        } else {
          flagMethods.put(name, new MethodAndParser<>(method, parser));
          returns.put(method, value); // Set the default value now so it can be overridden later.
        }
      }
    }

    // Then run through the arguments and parse the ones that have a relevant flag.
    Iterator<String> it = Arrays.asList(args).iterator();
    while (it.hasNext()) {
      String flag = it.next();

      if (flag.length() < 3 || !flag.substring(0, 2).equals("--")) {
        throw new UnknownFormatFlagsException("Flag '" + flag + "' should be preceded by '--'.");
      }

      flag = flag.substring(2, flag.length());
      if (!flagMethods.containsKey(flag)) {
        throw new UnknownFormatFlagsException("Flag '" + flag + "' is unkown to this application.");
      }

      MethodAndParser methodFlagParserPair = flagMethods.get(flag);

      if (methodFlagParserPair != null) {
        Method method = methodFlagParserPair.method;
        String parseToken = "";
        if (!(returns.get(method) instanceof Boolean) && it.hasNext()) {
          parseToken = it.next();
        }
        returns.put(method, methodFlagParserPair.flagParser.parseFlag(parseToken));
      }
    }

    @SuppressWarnings("unchecked")
    T flagsImpl =
        (T)
            Proxy.newProxyInstance(
                CommandLineParser.class.getClassLoader(),
                new Class[] {flagsClass},
                new FlagImplementor(returns));
    return flagsImpl;
  }

  private static class FlagImplementor implements InvocationHandler {

    private final Map<Method, Object> returns;

    FlagImplementor(Map<Method, Object> returns) {
      this.returns = returns;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
      return returns.get(method);
    }
  }

  private static final class MethodAndParser<T> {
    private final Method method;
    private final FlagParser<T> flagParser;

    private MethodAndParser(Method method, FlagParser<T> flagParser) {
      this.method = method;
      this.flagParser = flagParser;
    }
  }
}
