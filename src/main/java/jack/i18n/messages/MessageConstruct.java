package jack.i18n.messages;

import jack.i18n.messages.formatters.VariableFormatErrorException;

import java.time.ZoneId;
import java.util.Locale;
import java.util.Map;

/**
 * A message constructor is an abstraction that allows its children to take locale, zone and a given context
 * and construct part of a message.
 *
 * @author Guy Raz Nir
 * @since 2024/10/11.
 */
abstract sealed class MessageConstruct
        permits TextMessageConstruct, FormatterVariableConstruct, DynamicFormatterVariableConstruct {

    /**
     * <p>
     * Construct part of a message from a given <i>locale</i>, <i>zoneId</i> and <i>context</i>.
     * </p>
     * The implementation should always assume that all arguments are non-{@code null}.
     *
     * @param locale  Locale to format message part by.
     * @param zoneId  Zone to align message part by.
     * @param context Context to extract variables from.
     * @return Text.
     */
    public abstract String construct(Locale locale, ZoneId zoneId, Map<String, Object> context)
            throws VariableFormatErrorException;

}
