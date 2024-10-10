package jack.i18n.messages.formatters;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Date;
import java.util.Locale;

/**
 * <p>A formatter that handles dates, such as {@link LocalDate}, {@link Instant}, {@link LocalDateTime} (takes only the
 * <i>date</i> part) and classic {@link Date}.
 * </p>
 * Prior to formatting, the date is adjusted based on the given zone.
 *
 * @author Guy Raz Nir
 * @since 2024/10/01
 */
public class DateFormatter extends Formatter {

    /**
     * Class constructor.
     */
    public DateFormatter() {
        super(LocalDate.class, Instant.class, LocalDateTime.class, Date.class);
    }

    @Override
    protected String formatValue(Locale locale, ZoneId zoneId, Object value) throws FormatErrorException {
        LocalDate localDate;

        //
        // Convert given object to LocalDate. Adjust based on given zone, if required.
        //
        if (value instanceof LocalDate date) {
            localDate = date;
        } else if (value instanceof Instant instant) {
            localDate = instant.atZone(zoneId).toLocalDate();
        } else if (value instanceof LocalDateTime localDateTime) {
            localDate = localDateTime.toLocalDate();
        } else if (value instanceof Date date) {
            localDate = date.toInstant().atZone(zoneId).toLocalDate();
        } else {
            // For programmatic reason (BUG!), we got an unsupported type.
            throw new FormatErrorException("Unsupported date type: " + value.getClass());
        }

        return DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT).withLocale(locale).format(localDate);
    }
}
