package uk.me.m0rjc.cdiSettingsTool;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Documented;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Marker annotation for a configuration property.
 *
 * @author "Richard Corfield <m0rjc@m0rjc.me.uk>"
 */
@Inherited
@Target({ METHOD, FIELD })
@Retention(RUNTIME)
@Documented
public @interface ConfigProperty
{

}
