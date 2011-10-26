package uk.me.m0rjc.cdiSettingsTool.propertyDecoders;

import java.lang.reflect.Type;
import java.util.Date;

import uk.me.m0rjc.cdiSettingsTool.ConfigurationException;
import uk.me.m0rjc.cdiSettingsTool.ConfigurationValue;
import uk.me.m0rjc.cdiSettingsTool.PropertyDecoder;

/**
 * Decoder for {@link Date java.util.Date}.
 *
 * @author "Richard Corfield &lt;m0rjc@m0rjc.me.uk&gt;"
 */
public class DatePropertyDecoder implements PropertyDecoder<Date>
{
    @Override
    public Type[] getPropertyTypes()
    {
        return new Type[] {
                Date.class
        };
    }

    @Override
    public Date decode(final ConfigurationValue value)
            throws ConfigurationException
    {
        return value.getDateValue();
    }
}
