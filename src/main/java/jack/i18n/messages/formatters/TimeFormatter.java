package jack.i18n.messages.formatters;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;

/**
 * <p>A formatter that handles time objects such as {@link LocalTime}, or {@link LocalDateTime} (takes only the
 * <i>time</i> part).
 * </p>
 * Prior to formatting, the date is adjusted based on the given zone.
 *
 * @author Guy Raz Nir
 * @since 2024/10/01
 */
public class TimeFormatter extends Formatter {

    /**
     * Class constructor.
     */
    public TimeFormatter() {
        super(LocalTime.class, LocalDateTime.class);
    }

    @Override
    protected String formatValue(Locale locale, ZoneId zoneId, Object value) throws FormatErrorException {
        LocalTime localTime;

        //
        // Convert given object to LocalTime.
        //
        if (value instanceof LocalTime time) {
            localTime = time;
        } else if (value instanceof LocalDateTime localDateTime) {
            localTime = localDateTime.toLocalTime();
        } else {
            // For programmatic reason (BUG!), we got an unsupported type.
            throw new FormatErrorException("Unsupported date type: " + value.getClass());
        }

        return DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT).withLocale(locale).format(localTime);
    }
}
