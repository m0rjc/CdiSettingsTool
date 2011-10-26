package uk.me.m0rjc.cdiSettingsTool.propertyDecoders;

import java.lang.reflect.Type;

import uk.me.m0rjc.cdiSettingsTool.ConfigurationException;
import uk.me.m0rjc.cdiSettingsTool.ConfigurationValue;
import uk.me.m0rjc.cdiSettingsTool.PropertyDecoder;

/**
 * Decoder for Double and double properties.
 *
 * @author "Richard Corfield &lt;m0rjc@m0rjc.me.uk&gt;"
 */
public class DoublePropertyDecoder implements PropertyDecoder<Double>
{
    @Override
    public Type[] getPropertyTypes()
    {
        return new Type[] {
                Double.class,
                Double.TYPE
        };
    }

    @Override
    public Double decode(final ConfigurationValue value)
            throws ConfigurationException
    {
        return value.getDoubleValue();
    }
}
