package jack.i18n.messages.formatters;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.util.Locale;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * A set of test cases for{@link IntegerFormatterFactory} and {@link IntegerFormatter}.
 *
 * @author Guy Raz Nir
 * @since 2024/10/01
 */
public class IntegerFormatterTest {

    /**
     * Test that the formatter formats a value based on the given locale.
     */
    @Test
    @DisplayName("Test should format decimal value based on locale")
    public void testShouldFormatBasedOnLocale() {
        IntegerFormatter formatter = new IntegerFormatterFactory().createFormatter(null);

        // US locale uses comma as thousands separator.
        assertThat(formatter.formatValue(Locale.US, ZoneId.systemDefault(), 1234L)).isEqualTo("1,234");

        // French Canadian locale uses space (in Java -- non-breaking space 0x00A0) for thousands separator.
        assertThat(formatter.formatValue(Locale.CANADA_FRENCH, ZoneId.systemDefault(), 1234)).isEqualTo("1\u00A0234");
    }
}
