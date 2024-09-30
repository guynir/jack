package jack.i18n.messages.formatters;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Validates that a given properties set of type <i>Map<String, String></i> contains only known properties.
 */
public class RestrictedPropertiesValuesValidator {

    private final Set<String> allowedValues;

    public RestrictedPropertiesValuesValidator(String... allowedValues) {
        this(List.of(allowedValues));
    }

    public RestrictedPropertiesValuesValidator(Collection<String> allowedValues) {
        // Convert all allowed values to lowercase. Remove any essential leading/trailing spaces.
        this.allowedValues = allowedValues
                .stream()
                .filter(Objects::nonNull)
                .map(v -> v.toLowerCase().trim())
                .collect(Collectors.toSet());
    }

    /**
     * Validate that <i>properties</i> contains only known properties.
     *
     * @param properties Properties set to assert.
     * @throws FormatErrorException If <i>properties</i> contains properties that are unknown/unsupported.
     */
    public void validate(Map<String, String> properties) throws FormatErrorException {
        Set<String> unknownValues = properties
                .keySet()
                .stream()
                .filter(prop -> !allowedValues.contains(prop.toLowerCase().trim()))
                .collect(Collectors.toSet());

        if (!unknownValues.isEmpty()) {
            String message;
            if (unknownValues.size() == 1) {
                message = "Unknown property: " + unknownValues.iterator().next();
            } else {
                message = "Unknown properties: " + String.join(", ", unknownValues);
            }
            throw new FormatErrorException(message);
        }
    }

}
