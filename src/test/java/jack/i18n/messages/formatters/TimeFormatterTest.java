package jack.i18n.messages.formatters;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Locale;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * Test cases for {@link TimeFormatter}.
 *
 * @author Guy Raz Nir
 * @since 2024/10/07
 */
public class TimeFormatterTest {

    private final TimeFormatterFactory factory = new TimeFormatterFactory();

    /**
     * Test a simple formatting o given date (HH:mm).
     */
    @Test
    @DisplayName("Test should produce a short time string")
    public void testShouldProduceSimpleTimeString() {
        TimeFormatter formatter = factory.createFormatter();
        LocalTime localTime = LocalTime.of(13, 14, 15);

        assertThat(formatter.format(Locale.UK, ZoneId.of("GMT+2"), localTime)).isEqualTo("13:14");
    }
}
