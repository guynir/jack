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
     * Properties required for formatting values.
     *
     * @param decimalPlaces  Maximum number of decimal places. Must be non-negative value.
     * @param decimalPadding Number of decimal digits to pad. Must be non-negative value and not greater than
     *                       <i>decimalPlaces</i>.
     * @param rounding       {@code true} to round truncated decimal value, {@code false} if not.
     */
    public DecimalFormatter(int decimalPlaces, int decimalPadding, boolean rounding) {
        super(decimalPlaces, decimalPadding, rounding);
    }

    @Override
    protected String formatValue(Locale locale, ZoneId zoneId, Object value) throws FormatErrorException {
        return configureAndFormat(NumberFormat.getNumberInstance(locale), value);
    }
}
