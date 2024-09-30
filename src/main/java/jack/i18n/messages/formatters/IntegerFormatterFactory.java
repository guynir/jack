package jack.i18n.messages.formatters;

import java.util.Map;

public class IntegerFormatterFactory extends FormatterFactory<IntegerFormatter> {

    /**
     * Validator of restricted properties.
     */
    private static final RestrictedPropertiesValuesValidator validator = new RestrictedPropertiesValuesValidator();

    @Override
    public IntegerFormatter createFormatter(Map<String, String> properties) throws FormatErrorException {
        // In case 'properties' is defined, any property it may contain is considered 'unknown'.
        if (properties != null) {
            validator.validate(properties);
        }

        return new IntegerFormatter();
    }

}
