package uk.me.m0rjc.cdiSettingsTool;

/**
 * Marshall a property value from the configuration system to the target bean.
 *
 * @author "Richard Corfield &lt;m0rjc@m0rjc.me.uk&gt;"
 *
 * @param <I> Instance type of the target bean.
 * @param <T> Instance type of the Property.
 */
class PropertyHandler<I, T>
{
    /** Name of the property. */
    private final String m_propertyName;
    /** Type of the property. */
    private final Class<T> m_propertyType;
    /** Strategy to read the property. */
    private final PropertyDecoder<? extends T> m_decoder;
    /** Strategy to inject the property. */
    private final Injector<I, T> m_injector;
    /** Metadata for the property. */
    private final ConfigProperty m_metadata;

    /**
     * Constructor.
     * @param propertyClass class of the property to pass to the configuration provider.
     * @param propertyName name of the property to pass to the configuration provider.
     * @param decoder strategy to decode the property.
     * @param injector strategy to inject the property.
     */
    public PropertyHandler(final Class<T> propertyClass,
                    final String propertyName,
                    final ConfigProperty metadata,
                    final PropertyDecoder<? extends T> decoder,
                    final Injector<I, T> injector)
    {
        m_propertyName = propertyName;
        m_propertyType = propertyClass;
        m_decoder = decoder;
        m_injector = injector;
        m_metadata = metadata;
    }

    /**
     * Apply the property if there is a value.
     * @param provider the configuration provider.
     * @param configClassName class name to look up in the provider.
     * @param instance bean being set up.
     * @throws ConfigurationException if something goes wrong.
     */
    public void marshall(final ConfigurationProvider provider, final String configClassName, final I instance) throws ConfigurationException
    {
    	String requestClassName = firstNonNull(m_metadata.className(), configClassName);
    	String requestProperty = firstNonNull(m_metadata.property(), m_propertyName);
    	
        ConfigurationValue valueAccess = provider.getValue(requestClassName, requestProperty, m_propertyType);
        if(valueAccess != null)
        {
            T value = m_decoder.decode(valueAccess);
            m_injector.inject(instance, value);
        }
    }
    
    /**
     * If a is provided then return a, otherwise return b.
     * @param a the possible override value
     * @param b the default value
     * @return a if a not empty, otherwise b
     */
    private String firstNonNull(String a, String b)
    {
    	if(a != null && !a.isEmpty())
    	{
    		return a;
    	}
    	return b;
    }
}
