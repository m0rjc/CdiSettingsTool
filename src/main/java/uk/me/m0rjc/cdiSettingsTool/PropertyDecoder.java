package uk.me.m0rjc.cdiSettingsTool;

import java.lang.reflect.Type;
import java.util.ServiceLoader;

/**
 * Get a property from the configuration into the form for the target class.
 * PropertyDecoders are immutable, so can be used as flyweights.
 *
 * PropertyDecoders are found using the {@link ServiceLoader} mechanism.
 *
 * @author "Richard Corfield <m0rjc@m0rjc.me.uk>"
 *
 * @param <T> type of property resulting.
 */
public interface PropertyDecoder<T>
{
    /**
     * @return the Type that this decoder is for.
     */
    Type[] getPropertyTypes();

    /**
     * Extract a value for the property from the provider.
     * @param value provider's ConfigurationValue result.
     * @return a value for the property.
     * @throws ConfigurationException on error.
     */
    T decode(ConfigurationValue value) throws ConfigurationException;
}
