package uk.me.m0rjc.cdiSettingsTool;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.TimeZone;

import junit.framework.Assert;

import org.junit.Test;

import uk.me.m0rjc.cdiSettingsTool.configurationProviders.AbstractConfigurationValue;

/**
 * Test class for {@link AbstractConfigurationValue}.
 *
 * @author "Richard Corfield &lt;m0rjc@m0rjc.me.uk&gt;"
 */
public class TestAbstractConfigurationValue
{
    /**
     * Test values for True.
     * @throws ConfigurationException on error.
     */
    @Test
    public void testGetBooleanValueTrueValues() throws ConfigurationException
    {
        for(String value : new String[]{"true","yes","1","True","TRUE","YES","Yes"})
        {
            ConfigurationValue test = new ConcreteConfigurationValue(value);
            Assert.assertEquals(Boolean.TRUE, test.getBooleanValue());
        }
    }
    
    /**
     * Test values for False.
     * @throws ConfigurationException on error.
     */
    @Test
    public void testGetBooleanValueFalseValues() throws ConfigurationException
    {
        for(String value : new String[]{"false","no","0","False","FALSE","NO","No"})
        {
            ConfigurationValue test = new ConcreteConfigurationValue(value);
            Assert.assertEquals(Boolean.FALSE, test.getBooleanValue());
        }
    }
    
    /**
     * Test values for Boolean null.
     * @throws ConfigurationException on error.
     */
    @Test
    public void testGetBooleanNullValues() throws ConfigurationException
    {
        for(String value : new String[]{null,""})
        {
            ConfigurationValue test = new ConcreteConfigurationValue(value);
            Assert.assertNull(test.getBooleanValue());
        }        
    }
    
    /** 
     * Configuration error on error value.
     * @throws ConfigurationException on success!
     */
    @Test(expected=ConfigurationException.class)
    public void testGetBooleanErrorValue() throws ConfigurationException
    {
        ConfigurationValue test = new ConcreteConfigurationValue("wibble");
        test.getBooleanValue();
    }
    
    /** Test an Integer. */
    @Test
    public void testGetInteger() throws ConfigurationException
    {
        ConfigurationValue test = new ConcreteConfigurationValue("23");
        Assert.assertEquals(Integer.valueOf(23), test.getIntegerValue());
    }
    
    /** Test Get Integer for null values. */
    @Test
    public void testGetIntegerNullValues() throws ConfigurationException
    {
        for(String value : new String[]{null,""})
        {
            ConfigurationValue test = new ConcreteConfigurationValue(value);
            Assert.assertNull(test.getIntegerValue());
        }                
    }
    
    /** Test Get Integer for an error value. */
    @Test(expected=ConfigurationException.class)
    public void testGetIntegerErrorValue() throws ConfigurationException
    {
        ConfigurationValue test = new ConcreteConfigurationValue("This is not an integer!");
        test.getIntegerValue();        
    }
    
    /**
     * Test for Get Double.
     * @throws ConfigurationException on error.
     */
    @Test
    public void testGetDouble() throws ConfigurationException
    {
        String[] testValues =     {"0.0", "1", "100.0", "-123.4", "12.2e4"};
        double[] expectedValues = {0,      1,   100,     -123.4,  122000};
        
        for(int i = 0; i < testValues.length; i++)
        {
            ConcreteConfigurationValue value = new ConcreteConfigurationValue(testValues[i]);
            Assert.assertEquals(testValues[i], new Double(expectedValues[i]), value.getDoubleValue());
        }
        
        ConcreteConfigurationValue value = new ConcreteConfigurationValue("");
        Assert.assertNull(value.getDoubleValue());
    }
    
    @Test
    public void testGetDate() throws ConfigurationException
    {
        String[] testValues =     {
                "1999-01-05 12:32:59 +0000",
                "1979-12-25 00:00:00 +0000",
                "2040-01-01 12:00:00 +0200"
        };
   
        
        Date[] expectedValues = {
                DateTools.makeDateGmt(1999,01,05,12,32,59),
                DateTools.makeDateGmt(1979,12,25,00,00,00),
                DateTools.makeDateGmt(2040,01,01,10,00,00)
        };
        
        TimeZone systemDefault = TimeZone.getDefault();
        try
        {
            TimeZone.setDefault(TimeZone.getTimeZone("GMT"));
   
            for(int i = 0; i < testValues.length; i++)
            {
                ConcreteConfigurationValue value = new ConcreteConfigurationValue(testValues[i]);
                Assert.assertEquals(testValues[i], expectedValues[i], value.getDateValue());
            }
            
            TimeZone.setDefault(TimeZone.getTimeZone("GMT-0600"));

            for(int i = 0; i < testValues.length; i++)
            {
                ConcreteConfigurationValue value = new ConcreteConfigurationValue(testValues[i]);
                Assert.assertEquals(testValues[i], expectedValues[i], value.getDateValue());
            }
        }
        finally
        {
            // Restore the system timezone.
            TimeZone.setDefault(systemDefault);
        }
        
        ConcreteConfigurationValue value = new ConcreteConfigurationValue("");
        Assert.assertNull(value.getDateValue());        
    }
    
    @Test
    public void testGetURL() throws ConfigurationException, MalformedURLException
    {
    	for (String value : new String[]{"https://github.com/Pennine-View-Harrogate/CdiSettingsTool/tree/master/core", null})
        {
            ConfigurationValue test = new ConcreteConfigurationValue(value);
            Assert.assertEquals(value == null || value.equals("") ? null : new URL(value), test.getURLValue());
        }
    }
    
    @Test
    public void testGetCollection() throws ConfigurationException, MalformedURLException
    {
    	for (String value : new String[]{"a b c", "d e f ", null, ""})
        {
            ConfigurationValue test = new ConcreteConfigurationValue(value);
            Assert.assertEquals(value == null || value.equals("") ? null : Arrays.asList(value.split(" ")), test.getCollectionValue());
        }
    }
 
    /** Test class to inject strings into {@link AbstractConfigurationValue}. */
    private static class ConcreteConfigurationValue extends AbstractConfigurationValue
    {
        /** Value to return. */
        private String m_stringValue;

        /** @param stringValue value to return. */
        public ConcreteConfigurationValue(final String stringValue)
        {
            m_stringValue = stringValue;
        }

        @Override
        public String getStringValue() throws ConfigurationException
        {
            return m_stringValue;
        }
    }
    
}
