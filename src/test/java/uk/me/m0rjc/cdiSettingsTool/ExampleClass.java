package uk.me.m0rjc.cdiSettingsTool;

import javax.enterprise.context.Dependent;



/**
 * A class to test that settings can be injected.
 *
 * @author "Richard Corfield <m0rjc@m0rjc.me.uk>"
 */
@Dependent
public class ExampleClass
{
    /** An integer, set to 12 in the properties file. */
    @ConfigProperty
    private int m_integerSetting;
    
    /** A boolean, set to null in the properties file. */
    @ConfigProperty
    public Boolean m_booleanSetting = Boolean.FALSE;
    
    /** A string, set to "Hello World" in the properties file. */
    private String m_stringSetting;
    
    /** A string, not provided in the properties file. */
    @ConfigProperty
    private String m_absentSetting = "Default Value";
    
    /**
     * Setter method for the String property. 
     * @param value value to set.
     */
    @ConfigProperty
    public void setStringSetting(final String value)
    {
        m_stringSetting = value;
    }

    public int getIntegerSetting()
    {
        return m_integerSetting;
    }

    public void setIntegerSetting(int integerSetting)
    {
        m_integerSetting = integerSetting;
    }

    public String getStringSetting()
    {
        return m_stringSetting;
    }
    
    public Boolean getBooleanSetting()
    {
        return m_booleanSetting;
    }

    public String getAbsentSetting()
    {
        return m_absentSetting;
    }
}
