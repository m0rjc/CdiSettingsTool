package uk.me.m0rjc.cdiSettingsTool.test;

import javax.resource.spi.ConfigProperty;

/**
 * A class to test that settings can be injected.
 *
 * @author "Richard Corfield <m0rjc@m0rjc.me.uk>"
 */
public class TestClass
{
    @ConfigProperty
    private int integerSetting;
    
}
