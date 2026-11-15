package studio.yukilem.yukilem.checks;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CheckMetadata {
   CheckId id();

   String configRoot();

   String[] aliases() default {};

   boolean heuristic() default false;
}
