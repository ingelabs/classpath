package java.lang;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indicates that a method or constructor with a varargs parameter of
 * non-reifiable type does not perform unsafe operations on its varargs
 * parameter. Suppresses unchecked warnings at the declaration site and
 * at all call sites.
 *
 * @since 1.7
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.CONSTRUCTOR, ElementType.METHOD})
public @interface SafeVarargs
{
}
