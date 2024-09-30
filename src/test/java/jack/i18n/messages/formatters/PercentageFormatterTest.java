package jack.i18n.messages.formatters;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.util.Locale;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * A set of test cases for {@link PercentageFormatter}.
 *
 * @author Guy Raz Nir
 * @since 2024/10/01
 */
public class PercentageFormatterTest {

    /**
     * Formatter factory.
     */
    private final PercentageFormatterFactory factory = new PercentageFormatterFactory();

    /**
     * Test that the formatter formats a value based on the given locale.<br>
     * A percentage formatter will always double the given value in 100 (e.g.: 123 → 12,300).
     */
    @Test
    @DisplayName("Test should format decimal value based on locale")
    public void testShouldFormatBasedOnLocale() {
        PercentageFormatter formatter = factory.createFormatter();

        assertThat(formatter.formatValue(Locale.US, ZoneId.systemDefault(), 1234.4d)).isEqualTo("123,440%");

        // French locale uses narrow non-breaking space (NNBSP) for thousands separator and non-breaking space (NBSP)
        // between the value and the suffix percentage mark.
        assertThat(formatter.formatValue(Locale.FRENCH, ZoneId.systemDefault(), 1234.4)).isEqualTo("123\u202f440\u00a0%");
    }

}
