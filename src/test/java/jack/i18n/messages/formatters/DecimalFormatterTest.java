package jack.i18n.messages.formatters;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.util.Collections;
import java.util.Locale;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

/**
 * A set of test cases for {@link DecimalFormatter}.
 *
 * @author Guy Raz Nir
 * @since 2024/10/01
 */
public class DecimalFormatterTest {

    private final DecimalFormatterFactory factory = new DecimalFormatterFactory();

    /**
     * Test that the formatter formats a value based on the given locale.
     */
    @Test
    @DisplayName("Test should format decimal value based on locale")
    public void testShouldFormatBasedOnLocale() {
        DecimalFormatter formatter = factory.createFormatter();

        // US locale uses comma as thousands separator and dot for decimal separator.
        assertThat(formatter.formatValue(Locale.US, ZoneId.systemDefault(), 1234.456)).isEqualTo("1,234.45");

        // French Canadian locale uses space (in Java -- non-breaking space 0x00A0) for thousands separator and a comma
        // as decimal separator.
        assertThat(formatter.formatValue(Locale.CANADA_FRENCH, ZoneId.systemDefault(), 1234.456)).isEqualTo("1\u00A0234,45");
    }

    /**
     * Test that a decimal value which is limited by decimal places is truncated and rounded as expected.
     */
    @Test
    @DisplayName("Test should limit decimal places")
    public void testShouldLimitDecimalPlaces() {
        Map<String, String> properties = Collections.singletonMap("decimalPlaces", "3");
        DecimalFormatter formatter = factory.createFormatter(properties);

        // Decimal places .4567 is truncated into 3 digits and rounded up to 0.457.
        assertThat(formatter.formatValue(Locale.US, ZoneId.systemDefault(), 1234.4567d)).isEqualTo("1,234.456");
    }

    /**
     * Test should fail when an unknown/unsupported property is presented to a formatter.
     */
    @Test
    @DisplayName("Test should fail on unidentified property")
    public void testShouldFailOnUnidentifiedProperty() {
        final String UNKNOWN_PROPERTY = "someStrangeProperty";
        Map<String, String> properties = Collections.singletonMap(UNKNOWN_PROPERTY, "3");
        assertThatExceptionOfType(FormatErrorException.class)
                .isThrownBy(() -> factory.createFormatter(properties))
                .withMessageContaining(UNKNOWN_PROPERTY);
    }

    /**
     * Test should fail when an unknown/unsupported property is presented to a formatter.
     */
    @Test
    @DisplayName("Test should fail on invalid property value")
    public void testShouldFailOnInvalidPropertyValue() {
        final String PROPERTY_NAME = "decimalPlaces";
        Map<String, String> properties = Collections.singletonMap(PROPERTY_NAME, "invalidValue");
        assertThatExceptionOfType(FormatErrorException.class)
                .isThrownBy(() -> factory.createFormatter(properties))
                .withMessageContaining(PROPERTY_NAME);
    }

    /**
     * Test that when rounding is enabled for {@code DecimalFormatter}, values are rounded up when necessary.
     */
    @Test
    @DisplayName("Test should round up decimal value when necessary")
    public void testShouldRoundUpDecimalValueWhenNecessary() {
        // Create a formatter with 2 decimal points and no rounding.
        Map<String, String> properties = Map.of("decimalPlaces", "2", "rounding", "true");
        DecimalFormatter formatter = factory.createFormatter(properties);

        // Decimal value .556 should be rounded up to .56 after truncation from 3 decimal points to 2 decimal points.
        assertThat(formatter.formatValue(Locale.US, ZoneId.of("GMT"), 1234.556)).isEqualTo("1,234.56");

        // Decimal value .444 should NOT be rounded up after truncation from 3 decimal points to 2 decimal points.
        assertThat(formatter.formatValue(Locale.US, ZoneId.of("GMT"), 1234.444)).isEqualTo("1,234.44");

        // Create a formatter with no decimal points and rounding up.
        properties = Map.of(
                "decimalPlaces", "0",
                "rounding", "true"
        );
        formatter = factory.createFormatter(properties);

        // After truncating decimal digits, integer part should be increased by 1 after rounding.
        assertThat(formatter.formatValue(Locale.US, ZoneId.of("GMT"), 1234.556)).isEqualTo("1,235");
    }
}
