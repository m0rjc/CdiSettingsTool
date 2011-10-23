package uk.me.m0rjc.cdiSettingsTool;

import java.lang.reflect.Field;

/**
 * Implement {@link Injector} by setting a Field directly.
 *
 * @author "Richard Corfield &lt;m0rjc@m0rjc.me.uk&gt;"
 *
 * @param <I> Instance type of the target bean.
 * @param <T> Instance type of the Property.
 */
class FieldInjector<I, T> implements Injector<I, T>
{
    /** Method to use to set the property. */
    private final Field m_field;

    /**
     * @param field field to use to set the property.
     */
    public FieldInjector(final Field field)
    {
        m_field = field;
    }

    @Override
    public void inject(final I instance, final T value) throws ConfigurationException
    {
        try
        {
            m_field.setAccessible(true);
            m_field.set(instance, value);
        }
        catch (Exception e)
        {
            throw new ConfigurationException(e);
        }
    }

}
