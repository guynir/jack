package jack.i18n.messages;

import jack.i18n.messages.formatters.ContextAwareVariableFormatter;
import jack.i18n.messages.formatters.Formatter;
import jack.i18n.messages.formatters.VariableFormatErrorException;

import java.time.ZoneId;
import java.util.Locale;
import java.util.Map;

/**
 * A message construct that format a variable value based on a given formatter.
 *
 * @author Guy Raz Nir
 * @since 2024/10/19
 */
final class FormatterVariableConstruct extends MessageConstruct {

    /**
     * A formatter that selects the actual {@link Formatter} in runtime, based on variable's type.
     */
    private final ContextAwareVariableFormatter formatter;

    /**
     * Class constructor.
     *
     * @param variableName Name of variable for format.
     * @param formatter    Formatter to use.
     */
    FormatterVariableConstruct(String variableName, Formatter formatter) {
        this.formatter = new ContextAwareVariableFormatter(formatter, variableName);
    }

    @Override
    public String construct(Locale locale, ZoneId zoneId, Map<String, Object> context)
            throws VariableFormatErrorException {
        return formatter.format(locale, zoneId, context);
    }
}
