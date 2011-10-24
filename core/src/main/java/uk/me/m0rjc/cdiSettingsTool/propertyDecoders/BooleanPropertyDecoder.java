package uk.me.m0rjc.cdiSettingsTool.propertyDecoders;

import java.lang.reflect.Type;

import uk.me.m0rjc.cdiSettingsTool.ConfigurationException;
import uk.me.m0rjc.cdiSettingsTool.ConfigurationValue;
import uk.me.m0rjc.cdiSettingsTool.PropertyDecoder;

/**
 * Decoder for Boolean and boolean properties.
 *
 * @author "Richard Corfield &lt;m0rjc@m0rjc.me.uk&gt;"
 */
public class BooleanPropertyDecoder implements PropertyDecoder<Boolean>
{
    @Override
    public Type[] getPropertyTypes()
    {
        return new Type[] {
                Boolean.class,
                Boolean.TYPE
        };
    }

    @Override
    public Boolean decode(final ConfigurationValue value)
            throws ConfigurationException
    {
        return value.getBooleanValue();
    }

}
