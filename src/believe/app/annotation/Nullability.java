package believe.app.annotation;

import believe.app.annotation.NonnullByDefault.FieldsAreNonnulByDefault;
import believe.app.annotation.NonnullByDefault.ReturnTypesAreNonnulByDefault;
import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.meta.TypeQualifierDefault;
import javax.annotation.meta.TypeQualifierNickname;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@TypeQualifierNickname
@ParametersAreNonnullByDefault
@FieldsAreNonnulByDefault
@ReturnTypesAreNonnulByDefault
@Retention(RetentionPolicy.RUNTIME)
public @interface NonnullByDefault {
  @Nonnull
  @TypeQualifierDefault(ElementType.METHOD)
  @Retention(RetentionPolicy.RUNTIME)
  @interface ReturnTypesAreNonnulByDefault {}

  @Nonnull
  @TypeQualifierDefault(ElementType.FIELD)
  @Retention(RetentionPolicy.RUNTIME)
  @interface FieldsAreNonnulByDefault {}
}
