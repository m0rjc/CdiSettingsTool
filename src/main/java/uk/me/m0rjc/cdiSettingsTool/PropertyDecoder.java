package uk.me.m0rjc.cdiSettingsTool;

/**
 * Get a property from the configuration into the form for the target class.
 * PropertyDecoders are immutable, so can be used as flyweights.
 *
 * @author "Richard Corfield <m0rjc@m0rjc.me.uk>"
 *
 * @param <T> type of property resulting.
 */
interface PropertyDecoder<T>
{
    /**
     * Extract a value for the property from the provider.
     * @param value provider's ConfigurationValue result.
     * @return a value for the property.
     * @throws ConfigurationException on error.
     */
    T decode(ConfigurationValue value) throws ConfigurationException;
}
