package jack.i18n.messages.formatters;

import java.math.BigInteger;
import java.text.NumberFormat;
import java.time.ZoneId;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;


/**
 * A formatter that supports {@code java.lang.Integer}, {@code java.lang.Long}, {@code java.lang.Byte},
 * {@code java.lang.Short}, {@code java.util.concurrent.atomic.AtomicInteger} and
 * {@code java.util.concurrent.atomic.AtomicLong}.
 *
 * @author Guy Raz Nir
 * @since 2024/09/30
 */
public class IntegerFormatter extends Formatter {

    /**
     * Class constructor.
     */
    public IntegerFormatter() {
        super(Byte.class, Short.class, Integer.class, Long.class, AtomicInteger.class, AtomicLong.class, BigInteger.class);
    }

    @Override
    protected String formatValue(Locale locale, ZoneId zoneId, Object value) throws FormatErrorException {
        return NumberFormat.getIntegerInstance(locale).format(value);
    }

}
