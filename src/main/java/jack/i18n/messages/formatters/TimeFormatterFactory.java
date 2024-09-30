package jack.i18n.messages.formatters;

import java.util.Map;

/**
 * A factory for creating a {@link TimeFormatter}.
 *
 * @author Guy Raz Nir
 * @since 2024/10/02
 */
public class TimeFormatterFactory extends FormatterFactory<TimeFormatter> {

    @Override
    public TimeFormatter createFormatter(Map<String, String> properties) throws IllegalArgumentException, FormatErrorException {
        //
        // If case caller provided properties, we need to parse then and pre-configure formatter.
        //
        if (properties != null) {
            // To be continued ....
        }
        return new TimeFormatter();
    }
}
