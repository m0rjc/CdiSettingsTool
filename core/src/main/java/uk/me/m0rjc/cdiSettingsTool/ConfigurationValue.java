package uk.me.m0rjc.cdiSettingsTool;

/**
 * A Configured value.
 * Returned by {@link ConfigurationProvider}.
 *
 * @author "Richard Corfield &lt;m0rjc@m0rjc.me.uk&gt;" *
 */
public interface ConfigurationValue
{
    /**
     * @return the value as a String.
     * @throws ConfigurationException on error.
     */
    String getStringValue() throws ConfigurationException;

    /**
     * @return the value as a Boolean if applicable. True, False or null.
     * @throws ConfigurationException on error or the value is not a Boolean.
     */
    Boolean getBooleanValue() throws ConfigurationException;

    /**
     * @return the value as an Integer if applicable. True, False or null.
     * @throws ConfigurationException on error or the value is not Integer.
     */
    Integer getIntegerValue() throws ConfigurationException;
}
