package uk.me.m0rjc.cdiSettingsTool;

import java.net.URL;
import java.util.Collection;
import java.util.Date;

/**
 * A Configured value.
 * Returned by {@link ConfigurationProvider}.
 *
 * @author "Richard Corfield &lt;m0rjc@m0rjc.me.uk&gt;" *
 */
public interface ConfigurationValue
{
    /**
     * @return the value as a String.
     * @throws ConfigurationException on error.
     */
    String getStringValue() throws ConfigurationException;

    /**
     * @return the value as a Boolean if applicable. True, False or null.
     * @throws ConfigurationException on error or the value is not a Boolean.
     */
    Boolean getBooleanValue() throws ConfigurationException;

    /**
     * @return the value as an Integer if applicable.
     * @throws ConfigurationException on error or the value is not Integer.
     */
    Integer getIntegerValue() throws ConfigurationException;

    /**
     * @return the value as a Double if applicable.
     * @throws ConfigurationException on error or the value is not Double or Float.
     */
    Double getDoubleValue()  throws ConfigurationException;
    
    /**
     * @return the value as a Date if applicable.
     * @throws ConfigurationException on error or the value is not Date.
     */    
    Date getDateValue() throws ConfigurationException;
    
    /**
     * @return the value as a URL if applicable
     * @throws ConfigurationException on error or the value is not URL.
     */
    URL getURLValue() throws ConfigurationException;
    
    /**
     * @return the value as a Collection if applicable
     * @throws ConfigurationException on error or the value is not Collection.
     */
    Collection<String> getCollectionValue() throws ConfigurationException;
}
