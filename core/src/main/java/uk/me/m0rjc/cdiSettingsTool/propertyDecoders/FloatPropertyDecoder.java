package uk.me.m0rjc.cdiSettingsTool.propertyDecoders;

import java.lang.reflect.Type;

import uk.me.m0rjc.cdiSettingsTool.ConfigurationException;
import uk.me.m0rjc.cdiSettingsTool.ConfigurationValue;
import uk.me.m0rjc.cdiSettingsTool.PropertyDecoder;

/**
 * Decoder for Float and float properties.
 * This uses {@link ConfigurationValue#getDoubleValue()} to save on having too many
 * values to implement in ConfigurationValue.
 *
 * @author "Richard Corfield &lt;m0rjc@m0rjc.me.uk&gt;"
 */
public class FloatPropertyDecoder implements PropertyDecoder<Float>
{
    @Override
    public Type[] getPropertyTypes()
    {
        return new Type[] {
                Float.class,
                Float.TYPE
        };
    }

    @Override
    public Float decode(final ConfigurationValue value)
            throws ConfigurationException
    {
        Double doubleValue = value.getDoubleValue();
        if(doubleValue != null)
        {
            return new Float(doubleValue.floatValue());
        }
        return null;
    }
}
