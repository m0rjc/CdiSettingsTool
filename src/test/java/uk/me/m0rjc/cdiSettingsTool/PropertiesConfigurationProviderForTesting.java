package uk.me.m0rjc.cdiSettingsTool;

import java.io.InputStream;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;

import org.junit.Test;

import uk.me.m0rjc.cdiSettingsTool.configurationProviders.PropertiesConfigurationProvider;

import junit.framework.Assert;

/**
 * A CDI configuration provider bean.
 *
 * @author "Richard Corfield &lt;m0rjc@m0rjc.me.uk&gt;"
 */
public class PropertiesConfigurationProviderForTesting extends PropertiesConfigurationProvider
{
    /** An injection point to test that the system honours injection points. */
    private BeanManager m_beanManager;
    
    /** Empty constructor for CDI Proxy. */
    public PropertiesConfigurationProviderForTesting()
    {
        
    }
    
    /**
     * Injectable constructor to test constructor injection.
     * @param bm unused.
     */
    @Inject
    public PropertiesConfigurationProviderForTesting(final BeanManager bm)
    {
        initialise();
        getProperties().setProperty("uk.me.m0rjc.cdiSettingsTool.MarkerClass.injectConstructorUsed", "true");
    }
    
    /**
     * A test that method injection works.
     * @param bm ignored.
     */
    @Inject
    public void setBeanManager(final BeanManager bm)
    {
        getProperties().setProperty("uk.me.m0rjc.cdiSettingsTool.MarkerClass.hasBeanManager", "true");        
    }
    
    /**
     * Load the test properties.
     * (would be a PostConstruct, but that is being tested separately.)
     */
    private void initialise()
    {
        Properties p = new Properties();
        InputStream is = getClass().getResourceAsStream("/uk/me/m0rjc/cdiSettingsTool/configuration.properties");
        try
        {
            p.load(is);
            setProperties(p);
        }
        catch(Exception e)
        {
            throw new RuntimeException("Cannot load properties for test: " + e.getMessage(), e);
        }
    }
    
    /**
     * Place a special property to say that PostConstruct works.
     */
    @PostConstruct
    public void placeSpecialPostConstructProperty()
    {
        getProperties().setProperty("uk.me.m0rjc.cdiSettingsTool.MarkerClass.wasPostConstructed", "true");
    }
    
    /**
     * @return the BeanManager that should hopefully have been injected by CDI.
     */
    public BeanManager getBeanManager()
    {
        return m_beanManager;
    }
}
