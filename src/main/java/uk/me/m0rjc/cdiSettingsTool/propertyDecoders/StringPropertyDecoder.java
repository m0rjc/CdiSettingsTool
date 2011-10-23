package uk.me.m0rjc.cdiSettingsTool.propertyDecoders;

import java.lang.reflect.Type;

import uk.me.m0rjc.cdiSettingsTool.ConfigurationException;
import uk.me.m0rjc.cdiSettingsTool.ConfigurationValue;
import uk.me.m0rjc.cdiSettingsTool.PropertyDecoder;

/**
 * Decoder for String properties.
 *
 * @author "Richard Corfield &lt;m0rjc@m0rjc.me.uk&gt;"
 */
public class StringPropertyDecoder implements PropertyDecoder<String>
{
    @Override
    public Type[] getPropertyTypes()
    {
        return new Type[]{String.class};
    }

    @Override
    public String decode(final ConfigurationValue value) throws ConfigurationException
    {
        return value.getStringValue();
    }

}
