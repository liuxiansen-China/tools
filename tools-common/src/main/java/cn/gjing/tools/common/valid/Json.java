package cn.gjing.tools.common.valid;

import java.lang.annotation.*;

/**
 * @author Gjing
 * Marks the parameter as a Json object
 **/
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER,ElementType.FIELD})
@Documented
public @interface Json {
}
