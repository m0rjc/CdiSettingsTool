package uk.me.m0rjc.cdiSettingsTool;

import java.util.ArrayList;
import java.util.List;

/**
 * A ConfigurationProvider we can wire up after the fact.
 *
 * This provider supports multiple wrapped providers. If there are multiple then it will
 * return the first answer it gets.
 *
 * @author "Richard Corfield &lt;m0rjc@m0rjc.me.uk&gt;"
 */
final class LateBoundConfigurationProvider implements ConfigurationProvider
{
    /** Wrapped value. */
    private List<ConfigurationProvider> m_wrapped = new ArrayList<ConfigurationProvider>(1);

    /** @param wrapped set the ConfigurationProvider to wrap. */
    public void addWrapped(final ConfigurationProvider wrapped)
    {
        m_wrapped.add(wrapped);
    }

    /**
     * @return true if there is a ConfigurationProvider.
     */
    public boolean hasProvider()
    {
        return !m_wrapped.isEmpty();
    }

    @Override
    public ConfigurationValue getValue(final String targetClass,
            final String targetProperty, final Class<?> propertyType)
            throws ConfigurationException
    {
        for(ConfigurationProvider provider : m_wrapped)
        {
            ConfigurationValue value = provider.getValue(targetClass, targetProperty, propertyType);
            if(value != null)
            {
                return value;
            }
        }
        return null;
    }
}
