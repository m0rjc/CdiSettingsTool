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
 * @author "Richard Corfield <m0rjc@m0rjc.me.uk>"
 *
 * @param <X> the type being wrapped.
 */
final class ConfiguringInjectionTarget<X> implements InjectionTarget<X>
{
    /** Wrapped injection target. */
    private final InjectionTarget<X> m_wrapped;
    /** Source of configuration information. */
    private final ConfigurationProvider m_configurationProvider;
    /** Name of the class to look up in the properties provider. */
    private final String m_className;
    /** Property handlers. */
    private final List<PropertyHandler<X, ?>> m_properties;

    /**
     * Wrap the given injection target to add configuration information from the given provider.
     * @param it InjectionTarget to wrap.
     * @param provider source of configuration.
     * @param className the class name to look up in Configuration.
     * @param properties property handlers.
     */
    ConfiguringInjectionTarget(final InjectionTarget<X> it,
                               final ConfigurationProvider provider,
                               final String className,
                               final List<PropertyHandler<X, ?>> properties)
    {
        m_wrapped = it;
        m_configurationProvider = provider;
        m_className = className;
        m_properties = properties;
    }

    @Override
    public void inject(final X instance, final CreationalContext<X> ctx)
    {
        m_wrapped.inject(instance, ctx);
        injectConfiguration(instance);
    }

    /**
     * Inject the configuration into the given instance.
     *
     * @param instance target for injection.
     */
    private void injectConfiguration(final X instance)
    {
        try
        {
            for(PropertyHandler<X, ?> property : m_properties)
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
    public void postConstruct(final X instance)
    {
        m_wrapped.postConstruct(instance);
    }

    @Override
    public void preDestroy(final X instance)
    {
        m_wrapped.dispose(instance);
    }

    @Override
    public void dispose(final X instance)
    {
        m_wrapped.dispose(instance);
    }

    @Override
    public Set<InjectionPoint> getInjectionPoints()
    {
        return m_wrapped.getInjectionPoints();
    }

    @Override
    public X produce(final CreationalContext<X> ctx)
    {
        return m_wrapped.produce(ctx);
    }
}
