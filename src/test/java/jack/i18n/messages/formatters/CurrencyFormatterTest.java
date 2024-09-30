package jack.i18n.messages.formatters;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.util.Locale;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * A set of test cases for {@link CurrencyFormatter}.
 *
 * @author Guy Raz Nir
 * @since 2024/10/01
 */
public class CurrencyFormatterTest {

    /**
     * Israeli/Hebrew locale.
     */
    private static final Locale ISRAEL = new Locale("iw", "IL");
    /**
     * Factory for creating {@link CurrencyFormatter}.
     */
    private final CurrencyFormatterFactory factory = new CurrencyFormatterFactory();

    /**
     * Test that the formatter formats a value based on the given locale.
     */
    @Test
    @DisplayName("Test should format decimal value based on locale")
    public void testShouldFormatBasedOnLocale() {
        CurrencyFormatter formatter = factory.createFormatter();

        // US locale uses dollar sign as a prefix and rounds the decimal values (i.e.: 0.456 -> 0.46).
        assertThat(formatter.formatValue(Locale.US, ZoneId.systemDefault(), 1234.456)).isEqualTo("$1,234.45");

        // Israeli locale uses non-breaking space and a special character to indicate currency.
        // It also uses a RLM mark (right-to-left mark) as a prefix (do indicate the text direction should be read
        // right-to-left).
        assertThat(formatter.formatValue(ISRAEL, ZoneId.systemDefault(), 1234.456)).isEqualTo("\u200f1,234.45\u00a0₪");
    }

}
