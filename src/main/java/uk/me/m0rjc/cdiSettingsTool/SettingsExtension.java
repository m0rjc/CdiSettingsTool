package uk.me.m0rjc.cdiSettingsTool;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.spi.AnnotatedField;
import javax.enterprise.inject.spi.AnnotatedMethod;
import javax.enterprise.inject.spi.AnnotatedType;
import javax.enterprise.inject.spi.BeanManager;
import javax.enterprise.inject.spi.Extension;
import javax.enterprise.inject.spi.InjectionTarget;
import javax.enterprise.inject.spi.ProcessInjectionTarget;
import javax.resource.spi.ResourceAdapter;

/**
 * Extension to apply runtime settings to beans in CDI.
 *
 * @author "Richard Corfield <m0rjc@m0rjc.me.uk>"
 */
public class SettingsExtension implements Extension
{
    /** Decoders I know about. */
    private static final Map<Class<?>, PropertyDecoder<?>> DECODERS;
    static
    {
        HashMap<Class<?>, PropertyDecoder<?>> decoders = new HashMap<Class<?>, PropertyDecoder<?>>();
        decoders.put(String.class, new StringPropertyDecoder());
        DECODERS = Collections.unmodifiableMap(decoders);
    }

    /** Logging. */
    private Logger m_log = Logger.getLogger(SettingsExtension.class.getName());

    /**
     * Decorate an injection target if there are settings to apply.
     * @param pit the event instance from the CDI Container.
     * @param beanManager the global BeanManager.
     * @param <I> the instance type of the injection target bean.
     */
    public <I> void processInjectionTarget(@Observes final ProcessInjectionTarget<I> pit, final BeanManager beanManager)
    {
        final InjectionTarget<I> it = pit.getInjectionTarget();
        AnnotatedType<I> at = pit.getAnnotatedType();
        ConfigurationProvider provider = getProvider(beanManager);

        if(provider != null)
        {
            List<PropertyHandler<I, ?>> properties = getConfigurationProperties(at);
            if(properties != null && !properties.isEmpty())
            {
                InjectionTarget<I> wrapped = new ConfiguringInjectionTarget<I>(it, provider, at.getJavaClass().getName(), properties);
                pit.setInjectionTarget(wrapped);
            }
        }
    }

    /**
     * Determine which if any configuration properties apply to the bean.
     * @param annotatedType the type to find properties for.
     * @return a list of ready made PropertyHandler for the bean. Null or Empty indicate no configuration for this bean.
     * @param <I> the instance type of the bean.
     */
    private <I> List<PropertyHandler<I, ?>> getConfigurationProperties(final AnnotatedType<I> annotatedType)
    {
        Class<?> beanClass = annotatedType.getJavaClass();
        if(ResourceAdapter.class.isAssignableFrom(beanClass))
        {
            // Avoid ResourceAdapters. They have their own configuration.
            return null;
        }

        // Find property names.
        List<PropertyHandler<I, ?>> handlers = new ArrayList<PropertyHandler<I, ?>>();

        for(AnnotatedMethod<? super I> method : annotatedType.getMethods())
        {
            if(method.isAnnotationPresent(ConfigProperty.class))
            {
                PropertyHandler<I, ?> handler = constructMethodHandler(method);
                if(handler != null)
                {
                    handlers.add(handler);
                }
            }
        }

        for(AnnotatedField<? super I> field : annotatedType.getFields())
        {
            if(field.isAnnotationPresent(ConfigProperty.class))
            {
                PropertyHandler<I, ?> handler = constructFieldHandler(field);
                if(handler != null)
                {
                    handlers.add(handler);
                }
            }
        }

        return handlers;
    }

    /**
     * Construct a field handler for the given field.
     * @param field field to inject into.
     * @return the handler of null if not possible.
     * @param <I> instance type.
     */
    private <I> PropertyHandler<I, ?> constructFieldHandler(final AnnotatedField<? super I> field)
    {
        Class<? super I> declaringClass = field.getDeclaringType().getJavaClass();
        String declaringClassName = declaringClass.getName();

        Field javaField = field.getJavaMember();
        String name = decodeFieldPropertyName(javaField.getName());

        Class<?> propertyType = javaField.getType();
        PropertyDecoder<?> decoder = DECODERS.get(propertyType);
        if(decoder != null)
        {
            m_log.fine(String.format("Declared annotation property %s on class %s.", name, declaringClassName));
            return new PropertyHandler(propertyType, name, decoder, new FieldInjector(javaField));
        }

        m_log.severe(String.format("Cannot handle configuration field %s of class %s: Type %s not supported.", javaField.getName(), declaringClassName, propertyType.getName()));
        return null;
    }

    /**
     * Construct a field handler for the given field.
     * @param field field to inject into.
     * @return the handler of null if not possible.
     * @param <I> instance type.
     */
    private <I> PropertyHandler<I, ?> constructMethodHandler(final AnnotatedMethod<? super I> field)
    {
        Class<? super I> declaringClass = field.getDeclaringType().getJavaClass();
        String declaringClassName = declaringClass.getName();

        Method javaMethod = field.getJavaMember();
        String name = decodeMethodBeanName(javaMethod.getName());

        Class<?>[] parameterTypes = javaMethod.getParameterTypes();
        if(parameterTypes.length != 1)
        {
            m_log.severe(String.format("Cannot handle configuration method %s of class %s: Method must have one parameter.", javaMethod.getName(), declaringClassName));
            return null;
        }

        Class<?> propertyType = parameterTypes[0];
        PropertyDecoder<?> decoder = DECODERS.get(propertyType);
        if(decoder != null)
        {
            m_log.fine(String.format("Declared annotation property %s on class %s.", name, declaringClassName));
            return new PropertyHandler(propertyType, name, decoder, new MethodInjector(javaMethod));
        }

        m_log.severe(String.format("Cannot handle configuration method %s of class %s: Type %s not supported.", javaMethod.getName(), declaringClassName, propertyType.getName()));
        return null;
    }

    
    /**
     * Decode a field name to derive the property name.
     *
     * This implementation will remove m_ and s_.
     * @param name name of the field
     * @return name of the property.
     */
    private String decodeFieldPropertyName(final String name)
    {
        if(name.startsWith("m_") || name.startsWith("s_"))
        {
            return name.substring(2);
        }
        return name;
    }

    /**
     * Derive the bean name from a method.
     *
     * This implementation will remove get, set and is prefixes.
     *
     * @param name name of the method.
     * @return name of the property.
     */
    private String decodeMethodBeanName(final String name)
    {
        if(name.startsWith("get") || name.startsWith("set"))
        {
            return name.substring(3);
        }
        if(name.startsWith("is"))
        {
            return name.substring(2);
        }
        return name;
    }

    /**
     * Return the configuration provider instance to use.
     * @param beanManager the global bean manager.
     * @return the configuration provider, or null if there is none.
     */
    private ConfigurationProvider getProvider(final BeanManager beanManager)
    {
        // TODO Auto-generated method stub
        return null;
    }
}
