package uk.me.m0rjc.cdiSettingsTool;

import java.lang.reflect.Method;

/**
 * Injection using a setter Method.
 *
 * @author "Richard Corfield <m0rjc@m0rjc.me.uk>"
 *
 * @param <I> Instance type of the target bean.
 * @param <T> Instance type of the Property.
 */
class MethodInjector<I, T> implements Injector<I, T>
{
    /** Method to use to set the property. */
    private final Method m_setter;

    /**
     * @param setMethod method to use to set the property.
     */
    public MethodInjector(final Method setMethod)
    {
        m_setter = setMethod;
    }

    @Override
    public void inject(final I instance, final T value) throws ConfigurationException
    {
        try
        {
            m_setter.setAccessible(true);
            m_setter.invoke(instance, value);
        }
        catch (Exception e)
        {
            throw new ConfigurationException(e);
        }
    }
}
