package believe.app.flag_parsers;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface Flag {
  @Retention(RetentionPolicy.RUNTIME)
  @interface Integer {
    String name();
    int defaultValue() default 0;
  }

  @Retention(RetentionPolicy.RUNTIME)
  @interface Boolean {
    String name();
    boolean defaultValue() default false;
  }
}
