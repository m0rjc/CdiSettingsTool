package uk.me.m0rjc.cdiSettingsTool;

/**
 * The configuration provider will provide values for this class for diagnosis.
 *
 * @author "Richard Corfield &lt;m0rjc@m0rjc.me.uk&gt;"
 */
public class MarkerClass
{
    /** The BeanManager was injected. */
    @ConfigProperty
    public boolean hasBeanManager;
    
    /** PostConstruct was called. */
    @ConfigProperty
    public boolean wasPostConstructed;
    
    /** The Inject constructor was used. */
    @ConfigProperty
    public boolean injectConstructorUsed;
}
