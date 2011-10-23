package uk.me.m0rjc.cdiSettingsTool;

/**
 * A Configured value.
 * Returned by {@link ConfigurationProvider}.
 *
 * @author "Richard Corfield <m0rjc@m0rjc.me.uk>" *
 */
public interface ConfigurationValue
{
    /**
     * @return the value as a String.
     * @throws ConfigurationException on error.
     */
    String getStringValue() throws ConfigurationException;
}
