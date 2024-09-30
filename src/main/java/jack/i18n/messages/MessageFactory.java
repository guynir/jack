package jack.i18n.messages;

import jack.utils.Asserts;

import java.util.Locale;

/**
 * <p>A message factory is a centric element in generating, compiling and rendering locale-based custom messages.
 * </p>
 * <p>A message factory encapsulates within itself a locale and a set of <i>formatters</i> - strategies for
 * formatting various types, such as <i>numbers</i>, <i>dates</i>, <i>currencies</i> and much more.
 * </p>
 * When a message is <i>rendered</i>, it relies on this factory to provide it with locale and formatters.
 *
 * @author Guy Raz Nir
 * @since 2024/09/30
 */
public class MessageFactory {

    /**
     * Locale to use when rendering messages.
     */
    private Locale locale;

    /**
     * Default locale to use when non-other specified.
     */
    public static final Locale DEFAULT_LOCALE = Locale.ENGLISH;

    /**
     * Class constructor.
     */
    public MessageFactory() {
        this(DEFAULT_LOCALE);
    }

    /**
     * Class constructor.
     *
     * @param locale Initial locale to use.
     * @throws IllegalArgumentException If <i>locale</i> is {@code null}.
     */
    public MessageFactory(Locale locale) throws IllegalArgumentException {
        Asserts.notNull(locale, "Locale cannot be null.");
        this.locale = locale;
    }

    /**
     * @return Locale to use for rendering.
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * Sets a locale.
     *
     * @param locale Locale to use for rendering.
     * @throws IllegalArgumentException If <i>locale</i> is {@code null}.
     */
    public void setLocale(Locale locale) throws IllegalArgumentException {
        this.locale = locale;
    }
}
