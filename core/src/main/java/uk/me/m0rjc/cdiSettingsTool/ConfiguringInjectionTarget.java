package uk.me.m0rjc.cdiSettingsTool;

import java.util.List;
import java.util.Set;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.inject.InjectionException;
import javax.enterprise.inject.spi.InjectionPoint;
import javax.enterprise.inject.spi.InjectionTarget;

/**
 * Wrap InjectionTarget to add in configuration settings.
 *
 * @author "Richard Corfield &lt;m0rjc@m0rjc.me.uk&gt;"
 *
 * @param <I> the instance type being wrapped.
 */
final class ConfiguringInjectionTarget<I> implements InjectionTarget<I>
{
    /** Wrapped injection target. */
    private final InjectionTarget<I> m_wrapped;
    /** Source of configuration information. */
    private final LateBoundConfigurationProvider m_configurationProvider;
    /** Name of the class to look up in the properties provider. */
    private final String m_className;
    /** Property handlers. */
    private final List<PropertyHandler<I, ?>> m_properties;

    /**
     * Wrap the given injection target to add configuration information from the given provider.
     * @param it InjectionTarget to wrap.
     * @param provider source of configuration.
     * @param className the class name to look up in Configuration.
     * @param properties property handlers.
     */
    ConfiguringInjectionTarget(final InjectionTarget<I> it,
                               final LateBoundConfigurationProvider provider,
                               final String className,
                               final List<PropertyHandler<I, ?>> properties)
    {
        m_wrapped = it;
        m_configurationProvider = provider;
        m_className = className;
        m_properties = properties;
    }

    @Override
    public void inject(final I instance, final CreationalContext<I> ctx)
    {
        m_wrapped.inject(instance, ctx);
        if(m_configurationProvider.hasProvider())
        {
            injectConfiguration(instance);
        }
    }

    /**
     * Inject the configuration into the given instance.
     *
     * @param instance target for injection.
     */
    private void injectConfiguration(final I instance)
    {
        try
        {
            for(PropertyHandler<I, ?> property : m_properties)
            {
                property.marshall(m_configurationProvider, m_className, instance);
            }
        }
        catch (ConfigurationException e)
        {
            throw new InjectionException(e);
        }
    }

    @Override
    public void postConstruct(final I instance)
    {
        m_wrapped.postConstruct(instance);
    }

    @Override
    public void preDestroy(final I instance)
    {
        m_wrapped.dispose(instance);
    }

    @Override
    public void dispose(final I instance)
    {
        m_wrapped.dispose(instance);
    }

    @Override
    public Set<InjectionPoint> getInjectionPoints()
    {
        return m_wrapped.getInjectionPoints();
    }

    @Override
    public I produce(final CreationalContext<I> ctx)
    {
        return m_wrapped.produce(ctx);
    }
}
