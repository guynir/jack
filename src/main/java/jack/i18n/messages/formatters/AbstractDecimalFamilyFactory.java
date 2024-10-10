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
     * Property name that defines decimal points.
     */
    protected static final String DECIMAL_PLACES_PROPERTY = "decimalPlaces";

    /**
     * Validator of restricted properties.
     */
    protected static final RestrictedPropertiesValuesValidator validator =
            new RestrictedPropertiesValuesValidator(DECIMAL_PLACES_PROPERTY);


    @Override
    public final T createFormatter(Map<String, String> properties) throws FormatErrorException {
        Integer decimalPlaces = null;

        if (properties != null) {
            validator.validate(properties);

            decimalPlaces = parseValue(properties, DECIMAL_PLACES_PROPERTY, FormatterHelper::asInteger);
        }

        return createFormatterInternal(decimalPlaces);
    }

    /**
     * Underlying factory implementation override this method to create the actual {@code Formatter} instance.
     *
     * @param decimalPlaces Number of decimal places to allow. {@code null} if no limit is to be set.
     * @return New formatter.
     * @throws FormatErrorException If formatter could not be created due to properties' constraint violation.
     */
    protected abstract T createFormatterInternal(Integer decimalPlaces) throws FormatErrorException;


}
