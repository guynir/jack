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
    protected final int decimalPlaces;

    /**
     * <p>Number of decimal places to pad. For example, given <i>decimalPadding=3</i> and the value <i>1.24</i>, the
     * resulting text would be <i>1.240</i>.
     * </p>
     * This value should always be non-negative value and less than {@link #decimalPlaces}.
     */
    protected final int decimalPadding;

    /**
     * <p>Flag indicating if a truncated decimal value should be rounded or not. For example, if this property set to
     * {@code true} with <i>decimalPoints = 2</i> and a given value of <i>1.239999</i>, after formatting, the resulting
     * text is <i>1.24</i>.
     * </p>
     * If this flag is {@code false}, the value is simply truncated. E.g., with <i>decimalPoints = 2</i> and value
     * of <i>1.239999</i> the resulting text is <i>1.23</i>.
     */
    protected final boolean rounding;

    /**
     * Properties required for formatting values.
     *
     * @param decimalPlaces  Maximum number of decimal places. Must be non-negative value.
     * @param decimalPadding Number of decimal digits to pad. Must be non-negative value and not greater than
     *                       <i>decimalPlaces</i>.
     * @param rounding       {@code true} to round truncated decimal value, {@code false} if not.
     */
    public AbstractDecimalFamilyFormatter(int decimalPlaces, int decimalPadding, boolean rounding) {
        super(Float.class, Double.class, BigDecimal.class);
        this.decimalPlaces = decimalPlaces;
        this.decimalPadding = decimalPadding;
        this.rounding = rounding;
    }

    /**
     * Configure the formatter based on properties provided by the factory (such as {@link #decimalPlaces}).
     *
     * @param formatter Formatter to configure.
     * @return <i>formatter</i> argument.
     */
    protected NumberFormat configureFormatter(NumberFormat formatter) {
        formatter.setMaximumFractionDigits(decimalPlaces);
        formatter.setMinimumFractionDigits(decimalPadding);
        formatter.setRoundingMode(rounding ? RoundingMode.HALF_UP : RoundingMode.DOWN);
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
