package believe.testing.temporaryfolder;

import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@ExtendWith(TemporaryFolderExtension.class)
@Target(ElementType.METHOD)
public @interface UsesTemporaryFolder {}
