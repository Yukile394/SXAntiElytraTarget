package studio.yukilem.yukilem.libs.nixlib.events;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(value=RetentionPolicy.RUNTIME)
@Target(value={ElementType.METHOD})
public @interface Subscribe {
    public int priority() default 0;

    public boolean ignoreCancelled() default false;
}

