package jack.i18n.messages;

import java.text.NumberFormat;
import java.time.ZoneId;
import java.util.Locale;


/**
 * A formatter that formats an {@link java.lang.Number} into currency form, based on given locale.
 *
 * @author Guy Raz Nir
 * @since 2024/09/30
 */
public class CurrencyFormatter extends AbstractDecimalFamilyFormatter {

    /**
     * Class constructor.
     *
     * @param decimalPlaces Maximum number of decimal places. May be {@code null} when not relevant.
     */
    public CurrencyFormatter(Integer decimalPlaces) {
        super(decimalPlaces);
    }

    @Override
    protected String formatValue(Locale locale, ZoneId zoneId, Object value) throws FormatErrorException {
        return configureAndFormat(NumberFormat.getCurrencyInstance(locale), value);
    }
}
