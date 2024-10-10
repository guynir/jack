package jack.i18n.messages.formatters;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Locale;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Test cases for {@link DateFormatter}.
 *
 * @author Guy Raz Nir
 * @since 2024/10/07
 */
public class DateFormatterTest {

    private final DateFormatterFactory factory = new DateFormatterFactory();

    /**
     * Test a simple formatting o given date.
     */
    @Test
    @DisplayName("Test should produce a short date string")
    public void testShouldProduceSimpleDateString() {
        DateFormatter formatter = factory.createFormatter();
        LocalDate localDate = LocalDate.of(2024, 12, 31);

        assertThat(formatter.format(Locale.UK, ZoneId.of("GMT+2"), localDate)).isEqualTo("31/12/2024");
    }
}
