package uk.me.m0rjc.cdiSettingsTool.configurationProviders;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import uk.me.m0rjc.cdiSettingsTool.ConfigurationException;
import uk.me.m0rjc.cdiSettingsTool.ConfigurationValue;

/**
 * Support class for implementing {@link ConfigurationValue}. Allows an
 * implementor to implement the interface by only providing
 * {@link #getStringValue()}.
 * 
 * @author "Richard Corfield &lt;m0rjc@m0rjc.me.uk&gt;"
 */
public abstract class AbstractConfigurationValue implements ConfigurationValue
{
    /**
     * Date format used by the supplied implementation of {@link #getDateValue()}.
     */
    protected static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");

    @Override
    public abstract String getStringValue() throws ConfigurationException;

    @Override
    public Boolean getBooleanValue() throws ConfigurationException
    {
        String string = getStringValue();
        if (string == null || string.length() == 0)
        {
            return null;
        }
        string = string.toLowerCase();

        if ("true".equals(string) || "yes".equals(string) || "1".equals(string))
        {
            return Boolean.TRUE;
        }

        if ("false".equals(string) || "no".equals(string) || "0".equals(string))
        {
            return Boolean.FALSE;
        }

        throw new ConfigurationException(String.format(
                "Value %s not recognised as boolean", string));
    }

    @Override
    public Integer getIntegerValue() throws ConfigurationException
    {
        String string = getStringValue();
        if (string == null || string.length() == 0)
        {
            return null;
        }

        try
        {
            return Integer.parseInt(string);
        }
        catch (NumberFormatException e)
        {
            throw new ConfigurationException(e);
        }        

    }

    @Override
    public Double getDoubleValue() throws ConfigurationException
    {
        String string = getStringValue();
        if (string == null || string.length() == 0)
        {
            return null;
        }

        try
        {
            return Double.parseDouble(string);
        }
        catch (NumberFormatException e)
        {
            throw new ConfigurationException(e);
        }
    }

    /**
     * Very limited date parsing using {@link #DATE_FORMAT},
     * "yyyy-MM-dd HH:mm:ss Z".
     * 
     * An example date is "2011-20-26 21:00:00 +0000".
     * 
     * @return the parsed date.
     * @exception ConfigurationException on error.
     */
    @Override
    public Date getDateValue() throws ConfigurationException
    {
        String string = getStringValue();
        if (string == null || string.length() == 0)
        {
            return null;
        }

        try
        {
            return DATE_FORMAT.parse(string);
        }
        catch(ParseException p)
        {
            throw new ConfigurationException("Cannot parse date " + string + ".", p);                
        }
    }
}
