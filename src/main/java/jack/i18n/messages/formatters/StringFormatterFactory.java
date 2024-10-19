package jack.i18n.messages.formatters;

import java.util.Map;

/**
 * A factory that produces a {@link StringFormatter}.
 *
 * @author Guy Raz Nir
 * @since 2024/10/18
 */
public class StringFormatterFactory extends FormatterFactory<StringFormatter> {

    /**
     * Creates a new {@code StringFormatter}.
     *
     * @param properties Ignored.
     * @return New formatter.
     */
    @Override
    public StringFormatter createFormatter(Map<String, String> properties) {
        return new StringFormatter();
    }
}
