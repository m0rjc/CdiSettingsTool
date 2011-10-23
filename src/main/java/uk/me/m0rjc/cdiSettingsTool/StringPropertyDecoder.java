package uk.me.m0rjc.cdiSettingsTool;

/**
 * Decoder for String properties.
 *
 * @author "Richard Corfield <m0rjc@m0rjc.me.uk>"
 */
class StringPropertyDecoder implements PropertyDecoder<String>
{
    @Override
    public String decode(final ConfigurationValue value) throws ConfigurationException
    {
        return value.getStringValue();
    }

}
