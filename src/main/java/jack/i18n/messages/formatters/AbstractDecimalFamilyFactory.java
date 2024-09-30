package jack.i18n.messages.formatters;

import java.util.Map;

import static jack.i18n.messages.formatters.FormatterHelper.parseValue;

/**
 * <p>A common parent for all decimal-based formatter factories (such as {@code DecimalFormatterFactory},
 * {@code CurrencyFormatterFactory}, and {@code PercentageFormatterFactory}).
 * </p>
 * This implementation provides common logic to process properties and generate formatters.
 *
 * @author Guy Raz Nir
 * @since 2024/10/01
 */
public abstract class AbstractDecimalFamilyFactory<T extends Formatter> extends FormatterFactory<T> {

    /**
     * Property name that defines the maximum number of decimal digits.
     */
    protected static final String DECIMAL_PLACES_PROPERTY = "decimalPlaces";

    /**
     * Property name that defines how much padding to set.
     */
    protected static final String DECIMAL_PADDING_PROPERTY = "decimalPadding";

    /**
     * Property that defines whether to apply rounding or not on decimal-truncated values.
     */
    protected static final String ROUNDING_PROPERTY = "rounding";

    /**
     * Default value for decimal places, if non specified.
     */
    protected static final Integer DEFAULT_DECIMAL_PLACES = 2;

    /**
     * Default value for decimal padding, if none specified.
     */
    protected static final Integer DEFAULT_DECIMAL_PADDING = 0;

    /**
     * Default rounding control, if on specified.
     */
    protected static final Boolean DEFAULT_ROUNDING = false;

    /**
     * Validator of restricted properties.
     */
    protected static final RestrictedPropertiesValuesValidator validator =
            new RestrictedPropertiesValuesValidator(DECIMAL_PLACES_PROPERTY,
                    DECIMAL_PADDING_PROPERTY,
                    ROUNDING_PROPERTY);


    @Override
    public final T createFormatter(Map<String, String> properties) throws FormatErrorException {
        int decimalPlaces = DEFAULT_DECIMAL_PLACES;
        int decimalPadding = DEFAULT_DECIMAL_PADDING;
        boolean rounding = DEFAULT_ROUNDING;

        if (properties != null) {
            validator.validate(properties);

            decimalPlaces = parseValue(properties, DECIMAL_PLACES_PROPERTY, FormatterHelper::asInteger, DEFAULT_DECIMAL_PLACES);
            decimalPadding = parseValue(properties, DECIMAL_PLACES_PROPERTY, FormatterHelper::asInteger, DEFAULT_DECIMAL_PADDING);
            rounding = parseValue(properties, ROUNDING_PROPERTY, FormatterHelper::asBoolean, DEFAULT_ROUNDING);

            if (decimalPlaces < 0) {
                throw new FormatErrorException("'decimalPlaces' must be a non-negative integer.");
            }

            if (decimalPadding < 0) {
                throw new FormatErrorException("'decimalPadding' must be a non-negative integer.");
            }

            // Limit decimal padding to be no more than allowed decimal places.
            decimalPadding = Math.min(decimalPadding, decimalPlaces);
        }

        return createFormatterInternal(decimalPlaces, decimalPadding, rounding);
    }

    /**
     * Underlying factory implementation override this method to create the actual {@code Formatter} instance.
     *
     * @param decimalPlaces  Number of decimal places to allow.
     * @param decimalPadding How many decimal places to pad.
     * @param rounding       {@code true} to use math standard rounding mode, {@code false} - to apply no rounding while
     *                       truncating a decimal value.
     * @return New formatter.
     * @throws FormatErrorException If formatter could not be created due to properties' constraint violation.
     */
    protected abstract T createFormatterInternal(int decimalPlaces, int decimalPadding, boolean rounding)
            throws FormatErrorException;
}
