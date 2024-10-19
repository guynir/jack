package jack.i18n.messages;

import java.time.ZoneId;
import java.util.Locale;
import java.util.Map;

/**
 * A simple constructor that always returns the same text.
 *
 * @author Guy Raz Nir
 * @since 2024/10/11
 */
final class TextMessageConstruct extends MessageConstruct {

    /**
     * Static text to provide during {@link #construct(Locale, ZoneId, Map)} call.
     */
    private final String text;

    /**
     * Class constructor.
     *
     * @param text Static text to use.
     */
    TextMessageConstruct(String text) {
        this.text = text;
    }

    /**
     * Return the static text provided via constructor.
     *
     * @param locale  Not in use.
     * @param zoneId  Not in use.
     * @param context Not in use.
     * @return Static text.
     */
    @Override
    public String construct(Locale locale, ZoneId zoneId, Map<String, Object> context) {
        return text;
    }
}
