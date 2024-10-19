package jack.i18n.messages;

import jack.i18n.messages.formatters.VariableFormatErrorException;
import jack.utils.Asserts;

import java.io.Serializable;
import java.time.ZoneId;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Message implements Serializable {

    /**
     * The parent of the message. Used for extracting locale and zone.
     */
    private final MessageFactory parent;

    /**
     * List of constructs that can compose the entire message.
     */
    private final List<MessageConstruct> constructs;

    /**
     * Class constructor.
     *
     * @param parent     The factory who created this message.
     * @param constructs List of constructs.
     */
    Message(MessageFactory parent, List<MessageConstruct> constructs) {
        this.parent = parent;
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
        return render(parent.getLocale(), parent.getZoneId(), context);
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

    public String toString() {
        return "[Locale: " + parent.getLocale().toString() + "; ZoneId: " + parent.getZoneId().toString() + "]";
    }
}
