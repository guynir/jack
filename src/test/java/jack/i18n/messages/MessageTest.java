package jack.i18n.messages;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.util.Locale;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class MessageTest {

    /**
     * Test that a simple template is compiled and rendered successfully.
     */
    @Test
    @DisplayName("Test should produce a  message")
    public void testShouldProduceMessage() {
        MessageFactory messageFactory = MessageFactory.createDefault();
        Message message = messageFactory.compile("Mr. ${lastName} is ${age} years old.");

        Map<String, Object> context = Map.of(
                "firstName", "Sherlock",
                "lastName", "Holmes",
                "age", 60);

        String text = message.render(context);

        assertThat(text).isEqualTo("Mr. Holmes is 60 years old.");
    }

    /**
     * Test that for different locales the same message produces a localized message.
     */
    @Test
    @DisplayName("Test should produce localized message")
    public void testShouldProduceLocalizedMessage() {
        Map<String, Object> context = Map.of("distance", 1500000000.12D);

        MessageFactory messageFactory = MessageFactory.createDefault(Locale.US);
        Message message = messageFactory.compile("The sun is ${distance}KM from earth.");

        // US-based English should produce a locale distance value of '1,500,000,000.12'.
        String text = message.render(context);
        assertThat(text).isEqualTo("The sun is 1,500,000,000.12KM from earth.");

        // A French locale should produce a distance value of '1<NNBSP>500<NNBSP>000<NNBSP>000.12'
        // where NNBSP is a Narrow Non-Breaking Space character (Unicode u202F).
        text = message.render(Locale.FRENCH, ZoneId.of("GMT"), context);
        assertThat(text).isEqualTo("The sun is 1\u202F500\u202F000\u202F000,12KM from earth.");
    }

    /**
     * Test should use explicit formatter of type {@link jack.i18n.messages.formatters.DecimalFormatter} with
     * a set of non-default properties.
     */
    @Test
    @DisplayName("Test should render using explicit formatter")
    public void testShouldUseExplicitFormatter() {
        Map<String, Object> context = Map.of("price", 1.44);
        MessageFactory messageFactory = MessageFactory.createDefault(Locale.US);
        Message message = messageFactory.compile("${price;decimal;decimalPlaces=3;decimalPadding=3}");
        String text = message.render(context);

        // The original value was '1.44'. With our configuration, it should have 3 decimal places with padding.
        assertThat(text).isEqualTo("1.440");
    }
}
