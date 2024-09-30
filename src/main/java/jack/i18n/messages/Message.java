package jack.i18n.messages;

import jack.i18n.messages.formatters.VariableFormatErrorException;
import jack.utils.Asserts;

import java.io.Serializable;
import java.time.ZoneId;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

public class Message implements Serializable {

    /**
     * The parent of the message. Used for extracting locale and zone.
     */
    private final MessageRenderContext messageRenderContext;

    /**
     * The original message text compiled into this object.
     */
    private final String message;

    /**
     * List of constructs that can compose the entire message.
     */
    private final List<MessageConstruct> constructs;

    /**
     * Class constructor.
     *
     * @param messageRenderContext Message render context.
     * @param message              The raw message template compiled into this object.
     * @param constructs           List of constructs.
     */
    Message(MessageRenderContext messageRenderContext, String message, List<MessageConstruct> constructs) {
        this.messageRenderContext = messageRenderContext;
        this.message = message;
        this.constructs = constructs;
    }

    /**
     * Render a message using parent's locale and zone.
     *
     * @param context Context to extract necessary variables.
     * @return Rendered string.
     * @throws IllegalArgumentException     If <i>context</i> are {@code null}.
     * @throws VariableFormatErrorException If a message could not be rendered due to an error in format or missing
     *                                      variables.
     */
    public String render(Map<String, Object> context) throws IllegalArgumentException, VariableFormatErrorException {
        return render(messageRenderContext.getLocale(), messageRenderContext.getZoneId(), context);
    }

    /**
     * Render a message given a locale, zone and a context to extract variables from.
     *
     * @param locale  Locale to format variables values by.
     * @param zoneId  Zone to format variables values by.
     * @param context Context to extract necessary variables.
     * @return Rendered string.
     * @throws IllegalArgumentException     If either arguments are {@code null}.
     * @throws VariableFormatErrorException If a message could not be rendered due to an error in format or missing
     *                                      variables.
     */
    public String render(Locale locale, ZoneId zoneId, Map<String, Object> context)
            throws IllegalArgumentException, VariableFormatErrorException {
        Asserts.notNull(locale, "Locale cannot be null.");
        Asserts.notNull(zoneId, "ZoneId cannot be null.");
        Asserts.notNull(context, "Context cannot be null.");

        StringBuilder buf = new StringBuilder();
        for (MessageConstruct construct : constructs) {
            buf.append(construct.construct(locale, zoneId, context));
        }

        return buf.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Message other)) return false;
        return Objects.equals(messageRenderContext, other.messageRenderContext)
                && Objects.equals(message, other.message)
                && Objects.equals(constructs, other.constructs);
    }

    @Override
    public int hashCode() {
        return Objects.hash(messageRenderContext, message, constructs);
    }

    public String toString() {
        return getClass().getSimpleName() + " [Message: '" + message + "']";
    }
}
