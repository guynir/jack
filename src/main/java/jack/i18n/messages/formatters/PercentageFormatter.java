package jack.i18n.messages.formatters;

import java.text.NumberFormat;
import java.time.ZoneId;
import java.util.Locale;


/**
 * A formatter that formats an {@link Number} into currency form, based on given locale.
 *
 * @author Guy Raz Nir
 * @since 2024/09/30
 */
public class PercentageFormatter extends AbstractDecimalFamilyFormatter {

    /**
     * Properties required for formatting values.
     *
     * @param decimalPlaces  Maximum number of decimal places. Must be non-negative value.
     * @param decimalPadding Number of decimal digits to pad. Must be non-negative value and not greater than
     *                       <i>decimalPlaces</i>.
     * @param rounding       {@code true} to round truncated decimal value, {@code false} if not.
     */
    public PercentageFormatter(int decimalPlaces, int decimalPadding, boolean rounding) {
        super(decimalPlaces, decimalPadding, rounding);
    }

    @Override
    protected String formatValue(Locale locale, ZoneId zoneId, Object value) throws FormatErrorException {
        return configureAndFormat(NumberFormat.getPercentInstance(locale), value);
    }
}
