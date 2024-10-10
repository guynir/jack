package jack.i18n.messages.formatters;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;

/**
 * A common parent for all decimal-based formatters (such as {@code DecimalFormatter}, {@code CurrencyFormatter}, and
 * {@code PercentageFormatter}).
 *
 * @author Guy Raz Nir
 * @since 2024/10/01
 */
public abstract class AbstractDecimalFamilyFormatter extends Formatter {

    /**
     * Maximum number of decimals to allow. If this field is {@code null}, the formatter will not apply limitation
     * on decimal places.
     */
    protected final Integer decimalPlaces;

    /**
     * Properties required for formatting values.
     *
     * @param decimalPlaces Maximum number of decimal places. May be {@code null} when not relevant.
     */
    public AbstractDecimalFamilyFormatter(Integer decimalPlaces) {
        super(Float.class, Double.class, BigDecimal.class);
        this.decimalPlaces = decimalPlaces;
    }

    /**
     * Configure the formatter based on properties provided by the factory (such as {@link #decimalPlaces}).
     *
     * @param formatter Formatter to configure.
     * @return <i>formatter</i> argument.
     */
    protected NumberFormat configureFormatter(NumberFormat formatter) {
        if (decimalPlaces != null) {
            formatter.setMaximumFractionDigits(decimalPlaces);
            formatter.setRoundingMode(RoundingMode.HALF_UP);
        }
        return formatter;
    }

    /**
     * Configure a given {@code NumberFormat} formatter and format given value.
     *
     * @param formatter Formatter to configure.
     * @param value     Value to format.
     * @return String formatted representation of <i>value</i>.
     */
    protected String configureAndFormat(NumberFormat formatter, Object value) {
        return configureFormatter(formatter).format(value);
    }

}
