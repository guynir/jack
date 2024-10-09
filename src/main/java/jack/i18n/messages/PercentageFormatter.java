package jack.i18n.messages;

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
     * Class constructor.
     *
     * @param decimalPlaces Maximum number of decimal places. May be {@code null} when not relevant.
     */
    public PercentageFormatter(Integer decimalPlaces) {
        super(decimalPlaces);
    }

    @Override
    protected String formatValue(Locale locale, ZoneId zoneId, Object value) throws FormatErrorException {
        return configureAndFormat(NumberFormat.getPercentInstance(locale), value);
    }
}
