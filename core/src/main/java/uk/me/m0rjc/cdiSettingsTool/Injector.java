package uk.me.m0rjc.cdiSettingsTool;

/**
 * Strategy to inject a Y into an X.
 *
 * @author "Richard Corfield &lt;m0rjc@m0rjc.me.uk&gt;"
 *
 * @param <I> the target for injection.
 * @param <T> the type of thing being injected.
 */
interface Injector<I, T>
{
    /** Inject the value into the instance.
     *
     * @param instance target for injection.
     * @param value value to be injected.
     * @throws ConfigurationException on error.
     */
    void inject(I instance, T value) throws ConfigurationException;
}
