package jack.i18n.messages.formatters;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.util.Locale;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

/**
 * A set of test cases for {@link Formatter}.
 *
 * @author Guy Raz Nir
 * @since 2024/10/01
 */
public class FormatterTest {

    /**
     * Test should raise an exception if a non-matching value type is provided.
     */
    @Test
    @DisplayName("Test should fail on non decimal type")
    public void testShouldFailOnNonNumericValue() {
        assertThatExceptionOfType(FormatErrorException.class)
                .isThrownBy(() -> new DummyFormatter().format(Locale.US, ZoneId.systemDefault(), 1.0f));
    }

    /**
     * A dummy formatter class used for testing purposes only.
     */
    private static class DummyFormatter extends Formatter {

        /**
         * Class constructor.
         */
        public DummyFormatter() {
            super(Integer.class);
        }

        @Override
        protected String formatValue(Locale locale, ZoneId zoneId, Object value) throws FormatErrorException {
            return "";
        }
    }
}
