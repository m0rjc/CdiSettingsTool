package uk.me.m0rjc.cdiSettingsTool;

import javax.enterprise.inject.Instance;

import junit.framework.Assert;

import org.jboss.weld.environment.se.Weld;
import org.jboss.weld.environment.se.WeldContainer;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test in a CDI environment using Weld-SE.
 * 
 * @author "Richard Corfield &lt;m0rjc@m0rjc.me.uk&gt;"
 */
public class TestInjection
{
    /** Weld-SE instance. */
    private static Weld s_weld;
    /** Weld Container for testing. */
    private static WeldContainer s_weldContainer;
    
    /** Start Weld for this class. */
    @BeforeClass
    public static void startWeld()
    {
        s_weld = new Weld();
        s_weldContainer = s_weld.initialize();
    }
    
    /** Stop Weld after testing. */
    @AfterClass
    public static void stopWeld()
    {
        s_weld.shutdown();
    }
    
    /** Test that the test environment has found the configuration provider. 
     * @throws ConfigurationException */
    @Test
    public void testTestEnvironmentFindsConfigurationProvider() throws ConfigurationException
    {
        Instance<ConfigurationProvider> provider = s_weldContainer.instance().select(ConfigurationProvider.class);
        Assert.assertFalse("Unsatisfied", provider.isUnsatisfied());
        Assert.assertFalse("Ambigous", provider.isAmbiguous());
        
        ConfigurationProvider instance = provider.get();
        Assert.assertEquals("Hello World", instance.getValue("uk.me.m0rjc.cdiSettingsTool.ExampleClass", "stringSetting", String.class).getStringValue());
    }

    /**
     * Test that the sample class was configured.
     */
    @Test
    public void testSampleClassIsConfigured()
    {
        Instance<ExampleClass> provider = s_weldContainer.instance().select(ExampleClass.class);
        ExampleClass instance = provider.get();
        
        Assert.assertEquals("String setting", "Hello World", instance.getStringSetting());
        Assert.assertEquals("Integer setting", 12, instance.getIntegerSetting());
        Assert.assertNull("Boolean setting", instance.getBooleanSetting());
        Assert.assertEquals("Absent Setting", "Default Value", instance.getAbsentSetting());
    }
    
    /**
     * Test that the provider had its Injected property set.
     */
    @Test
    public void testProviderReceivedInjectedProperty()
    {
        Instance<MarkerClass> provider = s_weldContainer.instance().select(MarkerClass.class);
        MarkerClass instance = provider.get();
        
        Assert.assertTrue(instance.hasBeanManager);
    }

    /**
     * Test that the provider's postConstruct was called.
     */
    @Test
    public void testProviderWasPostConstructed()
    {
        Instance<MarkerClass> provider = s_weldContainer.instance().select(MarkerClass.class);
        MarkerClass instance = provider.get();
        
        Assert.assertTrue(instance.wasPostConstructed);
    }
    
    /**
     * Test that the provider's injection constructor was used.
     */
    @Test
    public void testProviderInjectConstructorUsed()
    {
        Instance<MarkerClass> provider = s_weldContainer.instance().select(MarkerClass.class);
        MarkerClass instance = provider.get();
        
        Assert.assertTrue(instance.injectConstructorUsed);        
    }
}
