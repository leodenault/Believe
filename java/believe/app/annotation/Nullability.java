package believe.app.annotation;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.meta.TypeQualifierDefault;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface Nullability {
  @Nonnull
  @TypeQualifierDefault(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @interface ReturnTypesAreNonnulByDefault {}

  @Nonnull
  @TypeQualifierDefault(ElementType.FIELD)
  @Retention(RetentionPolicy.RUNTIME)
  @interface FieldsAreNonnulByDefault {}

  static <T> T notNull(@Nullable T obj) {
    return obj;
  }

  static <T> T checkNotNull(@Nullable T obj) {
    if (obj == null) {
      throw new NullPointerException("Expected not to be null.");
    }
    return obj;
  }
}
