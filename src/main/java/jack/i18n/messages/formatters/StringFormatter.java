package jack.i18n.messages.formatters;

import java.time.ZoneId;
import java.util.Locale;

/**
 * A formatter for any deviant of {@link CharSequence} (such as {@link java.lang.String}).
 *
 * @author Guy Raz Nir
 * @since 2024/10/18
 */
public class StringFormatter extends Formatter {

    /**
     * Class constructor.
     */
    public StringFormatter() {
        super(CharSequence.class, String.class, StringBuffer.class, StringBuilder.class);
    }

    /**
     * Convert any {@code CharSequence} into a text.
     *
     * @param locale Locale to use for formatting/rendering.
     * @param zoneId Identifier of zone for formatting or adjusting values such as date and time.
     * @param value  Value to convert.
     * @return String.
     */
    @Override
    protected String formatValue(Locale locale, ZoneId zoneId, Object value) {
        return value.toString();
    }
}
