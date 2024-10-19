package jack.i18n.messages;

import jack.i18n.messages.formatters.ContextAwareVariableDynamicFormatter;
import jack.i18n.messages.formatters.Formatter;
import jack.i18n.messages.formatters.VariableFormatErrorException;

import java.time.ZoneId;
import java.util.Locale;
import java.util.Map;

/**
 * A message constructor that selects the right {@code Formatter} during message rendering based on
 */
final class DynamicFormatterVariableConstruct extends MessageConstruct {

    /**
     * A formatter that selects the actual {@link Formatter} in runtime, based on variable's type.
     */
    private final ContextAwareVariableDynamicFormatter formatter;

    /**
     * Class constructor.
     *
     * @param variableName  Name of variable for format.
     * @param formattersMap A map of formatters and their supported types.
     */
    DynamicFormatterVariableConstruct(String variableName, Map<Class<?>, Formatter> formattersMap) {
        formatter = new ContextAwareVariableDynamicFormatter(formattersMap, variableName);
    }

    @Override
    public String construct(Locale locale, ZoneId zoneId, Map<String, Object> context)
            throws VariableFormatErrorException {
        return formatter.format(locale, zoneId, context);
    }
}
