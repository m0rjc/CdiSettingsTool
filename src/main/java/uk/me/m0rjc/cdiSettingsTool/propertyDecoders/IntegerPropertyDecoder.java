package uk.me.m0rjc.cdiSettingsTool.propertyDecoders;

import java.lang.reflect.Type;

import uk.me.m0rjc.cdiSettingsTool.ConfigurationException;
import uk.me.m0rjc.cdiSettingsTool.ConfigurationValue;
import uk.me.m0rjc.cdiSettingsTool.PropertyDecoder;

/**
 * Decoder for Integer and int properties.
 *
 * @author "Richard Corfield <m0rjc@m0rjc.me.uk>"
 */
public class IntegerPropertyDecoder implements PropertyDecoder<Integer>
{
    @Override
    public Type[] getPropertyTypes()
    {
        return new Type[] {
                Integer.class,
                Integer.TYPE
        };
    }

    @Override
    public Integer decode(final ConfigurationValue value)
            throws ConfigurationException
    {
        return value.getIntegerValue();
    }

}
