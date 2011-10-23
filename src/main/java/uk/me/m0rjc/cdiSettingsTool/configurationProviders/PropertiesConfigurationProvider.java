package uk.me.m0rjc.cdiSettingsTool.configurationProviders;

import java.util.Properties;

import uk.me.m0rjc.cdiSettingsTool.ConfigurationException;
import uk.me.m0rjc.cdiSettingsTool.ConfigurationProvider;
import uk.me.m0rjc.cdiSettingsTool.ConfigurationValue;

/**
 * Tool to provide configuration from a Properties File.
 * Intended for subclassing or composition in code that knows were to get the
 * Properties from.
 * 
 * <p>Client code may either construct this and call {@link #setProperties(Properties)},
 * or it may subclass and override {@link #getProperties()}. The factory method {@link #createValue(String, String)}
 * is also provided to allow a subclass to specialise the value returned.
 *
 * @author "Richard Corfield &lt;m0rjc@m0rjc.me.uk&gt;"
 */
public class PropertiesConfigurationProvider implements ConfigurationProvider
{
    /** Backing Properties file. */
    private Properties m_properties;
    
    /** 
     * @return the backing properties file.
     * Subclasses may override this method.
     */
    public Properties getProperties()
    {
        return m_properties;
    }

    /**
     * Set the properties that will be returned by the implementation of {@link #getProperties()} in this class.
     * @param properties properties to use.
     */
    public final void setProperties(final Properties properties)
    {
        m_properties = properties;
    }

    @Override
    public ConfigurationValue getValue(final String targetClass,
            final String targetProperty,
            final Class<?> propertyType)
            throws ConfigurationException
    {
        if(m_properties != null)
        {
            String key = targetClass + "." + targetProperty;
            String value = m_properties.getProperty(key);
            if(value != null)
            {
                return createValue(key, value);
            }
        }
        return null;
    }

    /**
     * Create a Value.
     * @param propertyName the name of the property for reporting.
     * @param propertyValue the value read from the properties file.
     * @return a ConfigurationValue for the property.
     */
    protected ConfigurationValue createValue(final String propertyName, final String propertyValue)
    {
        return new Value(propertyName, propertyValue);
    }
    
    /** Value holder. */
    public static class Value extends AbstractConfigurationValue
    {
        /** Name of the property. */
        private final String m_propertyName;
       
        /** String value. */
        private final String m_stringValue;

        /**
         * @param propertyName name of the property for error reporting. 
         * @param stringValue Provide the given value as String value. 
         */
        public Value(final String propertyName, final String stringValue)
        {
            m_propertyName = propertyName;
            m_stringValue = stringValue;
        }
        
        @Override
        public String getStringValue() throws ConfigurationException
        {
            return m_stringValue;
        }

        /**
         * Decorate getBooleanValue to provide information about the offending property in any error message.
         * @return {@link #getStringValue()} as a Boolean or null.
         * @throws ConfigurationException if the cannot be parsed.
         */
        @Override
        public Boolean getBooleanValue() throws ConfigurationException
        {
            try
            {
                return super.getBooleanValue();
            }
            catch(ConfigurationException e)
            {
                throw new ConfigurationException("Error reading " + m_propertyName + ": " + e.getMessage(), e);
            }
        }

        /**
         * Decorate getIntegerValue to provide information about the offending property in any error message.
         * @return {@link #getStringValue()} as an Integer or null.
         * @throws ConfigurationException if the cannot be parsed.
         */
        @Override
        public Integer getIntegerValue() throws ConfigurationException
        {
            try
            {
                return super.getIntegerValue();
            }
            catch(ConfigurationException e)
            {
                throw new ConfigurationException("Error reading " + m_propertyName + ": " + e.getMessage(), e);
            }
        }
    }
}
