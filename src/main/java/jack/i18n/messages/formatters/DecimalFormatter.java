package jack.i18n.messages.formatters;

import java.text.NumberFormat;
import java.time.ZoneId;
import java.util.Locale;


/**
 * A formatter that supports {@code java.lang.Float} and {@code java.lang.Double} and {@code java.math.BigDecimal}.
 *
 * @author Guy Raz Nir
 * @since 2024/09/30
 */
public class DecimalFormatter extends AbstractDecimalFamilyFormatter {

    /**
     * Class constructor.
     *
     * @param decimalPlaces Maximum number of decimal places. May be {@code null} when not relevant.
     */
    public DecimalFormatter(Integer decimalPlaces) {
        super(decimalPlaces);
    }

    @Override
    protected String formatValue(Locale locale, ZoneId zoneId, Object value) throws FormatErrorException {
        return configureAndFormat(NumberFormat.getNumberInstance(locale), value);
    }
}
