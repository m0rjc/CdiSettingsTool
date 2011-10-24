package uk.me.m0rjc.cdiSettingsTool.configurationProviders;

import uk.me.m0rjc.cdiSettingsTool.ConfigurationException;
import uk.me.m0rjc.cdiSettingsTool.ConfigurationValue;

/**
 * Support class for implementing {@link ConfigurationValue}.
 * Allows an implementor to implement the interface by only providing {@link #getStringValue()}.
 *
 * @author "Richard Corfield &lt;m0rjc@m0rjc.me.uk&gt;"
 */
public abstract class AbstractConfigurationValue implements ConfigurationValue
{
    @Override
    public abstract String getStringValue() throws ConfigurationException;

    @Override
    public Boolean getBooleanValue() throws ConfigurationException
    {
        String string = getStringValue();
        if(string == null || string.length() == 0)
        {
            return null;
        }
        string = string.toLowerCase();

        if("true".equals(string) || "yes".equals(string) || "1".equals(string))
        {
            return Boolean.TRUE;
        }

        if("false".equals(string) || "no".equals(string) || "0".equals(string))
        {
            return Boolean.FALSE;
        }

        throw new ConfigurationException(String.format("Value %s not recognised as boolean", string));
    }

    @Override
    public Integer getIntegerValue() throws ConfigurationException
    {
        String string = getStringValue();
        if(string == null || string.length() == 0)
        {
            return null;
        }

        try
        {
            return Integer.parseInt(string);
        }
        catch(NumberFormatException e)
        {
            throw new ConfigurationException(e);
        }
    }
}
