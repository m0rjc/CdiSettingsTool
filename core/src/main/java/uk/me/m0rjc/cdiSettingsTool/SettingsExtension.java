package uk.me.m0rjc.cdiSettingsTool;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.logging.Logger;

import javax.enterprise.context.spi.CreationalContext;
import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AfterDeploymentValidation;
import javax.enterprise.inject.spi.AnnotatedField;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.Bean;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.InjectionTarget;
import javax.enterprise.inject.spi.ProcessInjectionTarget;
import javax.resource.spi.ResourceAdapter;

/**
 * Extension to apply runtime settings to beans in CDI.
 * 
 * @author "Richard Corfield &lt;m0rjc@m0rjc.me.uk&gt;"
 */
public class SettingsExtension implements Extension
{
    /** Decoders I know about. */
    private static Map<Type, PropertyDecoder<?>> s_decoders;

    /** Logging. */
    private Logger m_log = Logger.getLogger(SettingsExtension.class.getName());

    /** Late bound ConfigurationProvider that will be injected. */
    private LateBoundConfigurationProvider m_provider = new LateBoundConfigurationProvider();

    /** True if there is anything to configure in this deployment. */
    private boolean m_hasConfiguration;

    /**
     * Decorate an injection target if there are settings to apply.
     * 
     * @param pit
     *            the event instance from the CDI Container.
     * @param beanManager
     *            the global BeanManager.
     * @param <I>
     *            the instance type of the injection target bean.
     */
    public <I> void processInjectionTarget(
            @Observes final ProcessInjectionTarget<I> pit,
            final BeanManager beanManager)
    {
        final InjectionTarget<I> it = pit.getInjectionTarget();
        AnnotatedType<I> at = pit.getAnnotatedType();

        List<PropertyHandler<I, ?>> properties = getConfigurationProperties(at);
        if (properties != null && !properties.isEmpty())
        {
            InjectionTarget<I> wrapped = new ConfiguringInjectionTarget<I>(it,
                    m_provider, at.getJavaClass().getName(), properties);
            pit.setInjectionTarget(wrapped);
            m_hasConfiguration = true;
        }
    }

    /**
     * Locate and connect the configuration provider.
     * 
     * @param adv
     *            AfterDeploymentValidation event.
     * @param bm
     *            Bean manager.
     */
    public void connectProvider(@Observes final AfterDeploymentValidation adv,
            final BeanManager bm)
    {
        if (m_hasConfiguration)
        {
            Set<Bean<?>> beans = bm.getBeans(ConfigurationProvider.class);
            if (beans.size() == 0)
            {
                m_log.warning("No ConfigurationProviders found. Configuration will not be enabled.");
            }
            if (beans.size() > 1)
            {
                m_log.warning("Multiple ConfigurationProviders found. Results will be ambiguous");
            }

            for (Bean<?> bean : beans)
            {
                CreationalContext<?> context = bm.createCreationalContext(bean);
                ConfigurationProvider provider = (ConfigurationProvider) bm
                        .getReference(bean, ConfigurationProvider.class,
                                context);
                m_provider.addWrapped(provider);
                m_log.fine(String.format(
                        "Registered configuration provider %s", provider
                                .getClass().getName()));
            }
        }
    }

    /**
     * Determine which if any configuration properties apply to the bean.
     * 
     * @param annotatedType
     *            the type to find properties for.
     * @return a list of ready made PropertyHandler for the bean. Null or Empty
     *         indicate no configuration for this bean.
     * @param <I>
     *            the instance type of the bean.
     */
    private <I> List<PropertyHandler<I, ?>> getConfigurationProperties(
            final AnnotatedType<I> annotatedType)
    {
        Class<?> beanClass = annotatedType.getJavaClass();
        if (ResourceAdapter.class.isAssignableFrom(beanClass))
        {
            // Avoid ResourceAdapters. They have their own configuration.
            return null;
        }

        // Find property names.
        List<PropertyHandler<I, ?>> handlers = new ArrayList<PropertyHandler<I, ?>>();

        for (AnnotatedMethod<? super I> method : annotatedType.getMethods())
        {
            if (method.isAnnotationPresent(ConfigProperty.class))
            {
                PropertyHandler<I, ?> handler = constructMethodHandler(method);
                if (handler != null)
                {
                    handlers.add(handler);
                }
            }
        }

        for (AnnotatedField<? super I> field : annotatedType.getFields())
        {
            if (field.isAnnotationPresent(ConfigProperty.class))
            {
                PropertyHandler<I, ?> handler = constructFieldHandler(field);
                if (handler != null)
                {
                    handlers.add(handler);
                }
            }
        }

        return handlers;
    }

    /**
     * Construct a field handler for the given field.
     * 
     * @param field
     *            field to inject into.
     * @return the handler of null if not possible.
     * @param <I>
     *            instance type.
     */
    private <I> PropertyHandler<I, ?> constructFieldHandler(
            final AnnotatedField<? super I> field)
    {
        Class<? super I> declaringClass = field.getDeclaringType()
                .getJavaClass();
        String declaringClassName = declaringClass.getName();

        Field javaField = field.getJavaMember();
        String name = decodeFieldPropertyName(javaField.getName());

        Class<?> propertyType = javaField.getType();
        PropertyDecoder<?> decoder = getDecoders().get(propertyType);
        if (decoder != null)
        {
            m_log.fine(String.format(
                    "Declared annotation property %s on class %s.", name,
                    declaringClassName));
            return new PropertyHandler(propertyType, name, decoder,
                    new FieldInjector(javaField));
        }

        m_log.severe(String
                .format("Cannot handle configuration field %s of class %s: Type %s not supported.",
                        javaField.getName(), declaringClassName,
                        propertyType.getName()));
        return null;
    }

    /**
     * Construct a field handler for the given field.
     * 
     * @param field
     *            field to inject into.
     * @return the handler of null if not possible.
     * @param <I>
     *            instance type.
     */
    private <I> PropertyHandler<I, ?> constructMethodHandler(
            final AnnotatedMethod<? super I> field)
    {
        Class<? super I> declaringClass = field.getDeclaringType()
                .getJavaClass();
        String declaringClassName = declaringClass.getName();

        Method javaMethod = field.getJavaMember();
        String name = decodeMethodBeanName(javaMethod.getName());

        Class<?>[] parameterTypes = javaMethod.getParameterTypes();
        if (parameterTypes.length != 1)
        {
            m_log.severe(String
                    .format("Cannot handle configuration method %s of class %s: Method must have one parameter.",
                            javaMethod.getName(), declaringClassName));
            return null;
        }

        Class<?> propertyType = parameterTypes[0];
        PropertyDecoder<?> decoder = getDecoders().get(propertyType);
        if (decoder != null)
        {
            m_log.fine(String.format(
                    "Declared annotation property %s on class %s.", name,
                    declaringClassName));
            return new PropertyHandler(propertyType, name, decoder,
                    new MethodInjector(javaMethod));
        }

        m_log.severe(String
                .format("Cannot handle configuration method %s of class %s: Type %s not supported.",
                        javaMethod.getName(), declaringClassName,
                        propertyType.getName()));
        return null;
    }

    /**
     * Decode a field name to derive the property name.
     * 
     * This implementation will remove m_ and s_.
     * 
     * @param name
     *            name of the field
     * @return name of the property.
     */
    private String decodeFieldPropertyName(final String name)
    {
        return stripPrefix(name, "m_", "s_");
    }

    /**
     * Derive the bean name from a method.
     * 
     * This implementation will remove get, set and is prefixes.
     * 
     * @param name
     *            name of the method.
     * @return name of the property.
     */
    private String decodeMethodBeanName(final String name)
    {
        String result = stripPrefix(name, "get", "set", "is");
        return lowerCaseFirst(result);
    }

    /**
     * If the name starts with any one of the prefixes then remove it.
     * 
     * @param name
     *            name to process
     * @param prefixes
     *            list of prefixes to be removed
     * @return the string with any one of the prefixes removed. <!-- This method
     *         to stop CheckStyle complaining of magic numbers! -->
     */
    private String stripPrefix(final String name, final String... prefixes)
    {
        for (String prefix : prefixes)
        {
            if (name.startsWith(prefix))
            {
                return name.substring(prefix.length());
            }
        }
        return name;
    }

    /**
     * Make the first character lower case.
     * @param name name to convert
     * @return the name with first character lower case.
     */
    private String lowerCaseFirst(final String name)
    {
        StringBuilder sb = new StringBuilder();
        if(name.length() > 0)
        {
            sb.append(Character.toLowerCase(name.charAt(0)));
        }
        if(name.length() > 1)
        {
            sb.append(name.substring(1));
        }
        return sb.toString();
    }

    
    /**
     * @return the configured decoders.
     */
    private Map<Type, PropertyDecoder<?>> getDecoders()
    {
        if (s_decoders == null)
        {
            Map<Type, PropertyDecoder<?>> decoders = new HashMap<Type, PropertyDecoder<?>>();
            for (PropertyDecoder<?> decoder : ServiceLoader
                    .load(PropertyDecoder.class))
            {
                for (Type t : decoder.getPropertyTypes())
                {
                    PropertyDecoder<?> oldDecoder = decoders.put(t, decoder);
                    if (oldDecoder != null)
                    {
                        m_log.warning(String
                                .format("Duplicate property decoders defined for %s. %s overriding %s",
                                        t.toString(),
                                        decoder.getClass().getName(),
                                        oldDecoder.getClass().getName()));
                    }
                    m_log.fine(String.format("Registered property decoder for %s: %s", t.toString(), decoder.getClass().getName()));
                }
            }

            // Guard against building on a different thread.
            // The quick set is deemed harmless if it collides.
            if (s_decoders == null)
            {
                s_decoders = Collections.unmodifiableMap(decoders);
            }
        }
        return s_decoders;
    }
}
