package jack.i18n.messages.formatters;

import java.util.Map;

/**
 * A factory for creating a {@link DateFormatter}.
 *
 * @author Guy Raz Nir
 * @since 2024/10/02
 */
public class DateFormatterFactory extends FormatterFactory<DateFormatter> {

    @Override
    public DateFormatter createFormatter(Map<String, String> properties) throws IllegalArgumentException, FormatErrorException {
        //
        // If case caller provided properties, we need to parse then and pre-configure formatter.
        //
        if (properties != null) {
            // To be continued ....
        }
        return new DateFormatter();
    }
}
