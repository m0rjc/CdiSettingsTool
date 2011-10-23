package uk.me.m0rjc.cdiSettingsTool;

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
