package uk.me.m0rjc.cdiSettingsTool;

/**
 * Exceptions while configuring.
 *
 * @author "Richard Corfield &lt;m0rjc@m0rjc.me.uk&gt;"
 */
public class ConfigurationException extends Exception
{
    /** Class version for serialisation. */
    private static final long serialVersionUID = 1L;

    /** @see Exception#Exception() */
    public ConfigurationException()
    {
        super();
    }

    /**
     * @param message detail message
     * @param cause cause to wrap.
     * @see Exception#Exception(String, Throwable)
     */
    public ConfigurationException(final String message, final Throwable cause)
    {
        super(message, cause);
    }

    /**
     * @param message detail message
     * @see Exception#Exception(String)
     */
    public ConfigurationException(final String message)
    {
        super(message);
    }

    /**
     * @param cause cause to wrap.
     * @see Exception#Exception(Throwable)
     */
    public ConfigurationException(final Throwable cause)
    {
        super(cause);
    }
}
