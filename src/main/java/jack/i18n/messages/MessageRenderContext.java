package jack.i18n.messages;

import jack.utils.Asserts;

import java.time.ZoneId;
import java.util.Locale;
import java.util.Objects;

/**
 * <p>A message render context contains a {@code Locale} and a {@code ZoneId} required for rendering
 * {@link Message messages}.
 * </p>
 * Typically, a closed-locale messages will share the same message render context. In such a way, changing the locale
 * or zone in such a context will affect the rendering of all associated messages.
 *
 * @author Guy Raz Nir
 * @since 2024/10/23
 */
public class MessageRenderContext {

    /**
     * Locale to use.
     */
    private Locale locale;

    /**
     * Zone to use.
     */
    private ZoneId zoneId;

    /**
     * Class constructor.
     *
     * @param locale Locale to set for this context.
     * @param zoneId Zone to set for this context.
     * @throws IllegalArgumentException If either arguments are {@code null}.
     */
    public MessageRenderContext(Locale locale, ZoneId zoneId) throws IllegalArgumentException {
        this.locale = locale;
        this.zoneId = zoneId;
    }

    /**
     * @return Current locale (never {@code null}).
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * Sets the locale for this context.
     *
     * @param locale Locale to set.
     * @throws IllegalArgumentException If <i>locale</i> is {@code null}.
     */
    public void setLocale(Locale locale) {
        Asserts.notNull(locale, "Locale cannot be null.");
        this.locale = locale;
    }

    /**
     * @return Zone set for this context (never {@code null}).
     */
    public ZoneId getZoneId() {
        return zoneId;
    }

    /**
     * Sets zone for this context.
     *
     * @param zoneId Zone to set.
     * @throws IllegalArgumentException If <i>zoneId</i> is {@code null}.
     */
    public void setZoneId(ZoneId zoneId) throws IllegalArgumentException {
        Asserts.notNull(zoneId, "ZoneId cannot be null.");
        this.zoneId = zoneId;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof MessageRenderContext that)) return false;
        return Objects.equals(locale, that.locale) && Objects.equals(zoneId, that.zoneId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(locale, zoneId);
    }

    @Override
    public String toString() {
        return MessageRenderContext.class.getSimpleName() + " [Locale: " + locale + ";  ZoneId=" + zoneId + "]";
    }
}
