package jack.i18n.messages;

import jack.i18n.messages.formatters.FormatErrorException;
import jack.strings.StringScanner;
import jack.utils.Asserts;

import java.time.ZoneId;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * <p>Parse a token of the form <i>${varName;type;name1=value1;name2=value2;...}</i> variable name and list of properties.
 * </p>
 * <p>A token can either be vague or explicit.</p>
 * <p>A vague token contains only a variable name to be extracted from a context. The actual formatting will be
 * determined during a {@link Message#render(Locale, ZoneId, Map) message render phase}. For example:
 * <pre>
 *     Message message = factory.newMessage("The distance to the Sun is ${distance} KM.");
 * </pre>
 * The formatter used for <i>distance</i> variable is determined during render time.
 * </p>
 * <p>An explicit token contains a directive indicating the desired formatter and optional configuration properties.
 * For example:
 * <pre>
 *     Message message = factory.newMessage("The distance to the Sun is ${distance;decimal;decimalPlaces=2}.");
 * </pre>
 * The formatted used for <i>distance</i> is declared ahead as <i>decimal formatter</i> and accept the property
 * <i>decimalPlaces</i> with assigned value <i>2</i>.
 * </p>
 *
 * @author Guy Raz Nir
 * @since 2024/10/18
 */
public class TokenDefinitionParser {

    public static final String TOKEN_SPLITTER = ";";

    public static final String PROPERTY_SPLITTER = "=";

    /**
     * Describe a single token -- its binding variable, formatter to use and optionally - the formatter properties.
     *
     * @param variableName  Variable name.
     * @param formatterName Formatter to use.
     * @param properties    List of properties.
     */
    public record TokenDefinition(String variableName, String formatterName, Map<String, String> properties) {

        /**
         * @return {@code true} if token is explicit (specify which formatter to use). {@code false} if vague.
         */
        public boolean isExplicit() {
            return formatterName != null;
        }
    }

    /**
     * Class constructor.
     */
    public TokenDefinitionParser() {
    }

    /**
     * Parse a given token into
     *
     * @param token Token expression to parse.
     * @return Token definition.
     * @throws IllegalArgumentException If <i>token</i> is {@code null}.
     * @throws FormatErrorException     If either token does not contain variable name
     */
    public TokenDefinition parse(String token) throws IllegalArgumentException, FormatErrorException {
        Asserts.notNull(token, "A token cannot be null.");

        if (token.isEmpty()) {
            throw new FormatErrorException("Token cannot be empty.");
        }

        String[] parts = StringScanner.split(token, TOKEN_SPLITTER);

        String variableName = parts[0].trim();
        String formatterName = null;
        Map<String, String> properties = null;

        if (variableName.isEmpty()) {
            throw new FormatErrorException("Variable name is empty.");
        }

        // If we got only 1 part, the format of the token implies a vague token.
        // If we get more than 1 part, we need to treat the strings as follows:
        //   1. First string - variable name.
        //   2. Second string - formatter name.
        //   3. The rest of the strings are key=value pairs.
        if (parts.length > 1) {
            formatterName = parts[1].trim();
            if (formatterName.isEmpty()) {
                throw new FormatErrorException("Formatter name is empty.");
            }

            properties = new HashMap<>(parts.length - 2);
            for (int i = 2; i < parts.length; i++) {
                String[] keyValuePair = parseProperty(parts[i]);
                if (keyValuePair == null) {
                    throw new FormatErrorException("Property " + (i - 1) + " is invalid (" + parts[i] + "); must be key=value pair.");
                }
                properties.put(keyValuePair[0], keyValuePair[1]);
            }
        }

        return new TokenDefinition(parts[0], formatterName, properties);
    }

    protected String[] parseProperty(String property) {
        int offset = StringScanner.findSubstring(property, PROPERTY_SPLITTER);
        if (offset == -1) {
            return null;
        }
        return new String[]{property.substring(0, offset), property.substring(offset + PROPERTY_SPLITTER.length())};
    }
}
