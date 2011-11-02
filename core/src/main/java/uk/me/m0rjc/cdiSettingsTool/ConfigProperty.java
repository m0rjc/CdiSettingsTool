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
 * @author "Richard Corfield &lt;m0rjc@m0rjc.me.uk&gt;"
 */
@Inherited
@Target({ METHOD, FIELD })
@Retention(RUNTIME)
@Documented
public @interface ConfigProperty
{
	/**
	 * If you wish to specify the property name set it here. Otherwise leave alone
	 * to take the default.
	 */
	String property() default "";
	
	/**
	 * If you wish to override the class name that is used to request a property do so here.
	 */
	String className() default "";
	
	/**
	 * Some of our settings are things we'd not want to publish in documentation or a user
	 * interface. This defaults to true, but set it to false for those sneaky developer-only
	 * settings.
	 */
	boolean publish() default true;
}
