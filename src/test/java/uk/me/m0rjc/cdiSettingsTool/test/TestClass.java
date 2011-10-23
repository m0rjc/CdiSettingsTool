package uk.me.m0rjc.cdiSettingsTool.test;

import uk.me.m0rjc.cdiSettingsTool.ConfigProperty;


/**
 * A class to test that settings can be injected.
 *
 * @author "Richard Corfield <m0rjc@m0rjc.me.uk>"
 */
public class TestClass
{
    @ConfigProperty
    private int m_integerSetting;
    
    private String m_stringSetting;
    
    @ConfigProperty
    public void setStringSetting(String value)
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
    
}
