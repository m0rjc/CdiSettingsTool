package uk.me.m0rjc.cdiSettingsTool;

/**
 * Applications implement this interface to provide configuration.
 *
 * @author "Richard Corfield &lt;m0rjc@m0rjc.me.uk&gt;"
 */
public interface ConfigurationProvider
{
    /**
     * Return the value for the property, only if it can be satisfied.
     * @param targetClass the class being configured.
     * @param targetProperty the property being configured.
     * @param propertyType the required type.
     * @return the ConfigurationValue or null if not known.
     * @exception ConfigurationException on error or misconfiguration.
     */
    ConfigurationValue getValue(String targetClass, String targetProperty, Class<?> propertyType)
        throws ConfigurationException;
}
