package uk.me.m0rjc.cdiSettingsTool;

import java.util.Date;

import javax.enterprise.context.Dependent;



/**
 * A class to test that settings can be injected.
 *
 * @author "Richard Corfield &lt;m0rjc@m0rjc.me.uk&gt;"
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
    
    @ConfigProperty
    private float m_floatSetting;
    
    @ConfigProperty
    private double m_doubleSetting;
    
    @ConfigProperty
    private Float m_floatWrapperSetting;
    
    @ConfigProperty
    private Double m_doubleWrapperSetting;
    
    @ConfigProperty
    private Date m_dateSetting;
    
    @ConfigProperty(className="uk.me.m0rjc.SomewhereElse", property="theProperty")
    private int m_somewhereElseProperty;
    
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

    public float getFloatSetting()
    {
        return m_floatSetting;
    }

    public double getDoubleSetting()
    {
        return m_doubleSetting;
    }

    public Float getFloatWrapperSetting()
    {
        return m_floatWrapperSetting;
    }

    public Double getDoubleWrapperSetting()
    {
        return m_doubleWrapperSetting;
    }

    public Date getDateSetting()
    {
        return m_dateSetting;
    }

	public int getSomewhereElseProperty()
	{
		return m_somewhereElseProperty;
	}
}
