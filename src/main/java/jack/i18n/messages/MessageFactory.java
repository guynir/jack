package jack.i18n.messages;

import jack.i18n.messages.formatters.Formatter;
import jack.i18n.messages.formatters.*;
import jack.strings.*;
import jack.utils.Asserts;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

/**
 * <p>A message factory is a centric element in generating, compiling and rendering messages.
 * </p>
 * <p>A creation of message takes two phases:
 * <ul>
 *     <li>
 *         Message compilation - A string template, such as <i>Hello Mr. ${lastName}; today is ${todayDate}</i>,
 *         verify its placeholders' definitions and create an efficient compiled form that allows quick generation of
 *         texts.
 *         The first stage creates a new object of type {@link Message}.
 *      </li>
 *      <li>
 *          Message rendering - A {@code Message} object renders its contents rendering into a text.
 *          Rendering takes a locale and timezone and generates a text based on these properties.
 *      </li>
 * </ul>
 * </p>
 * <p>A message factory encapsulates A set of <i>formatters</i> - strategies for
 * formatting various types, such as <i>numbers</i>, <i>dates</i>, <i>currencies</i> and much more.
 * </p>
 * <p>There are two collections of formatters:
 * <ul>
 *     <li>Standard formatters - a map between a function name and a formatter.
 *         For example, using the following placeholder definition:<br>
 *         <i>Today is ${now,as=dayOfWeek}</i><br>
 *         will take a variable called <i>now</i> from an external context, fetch a formatter registered as
 *         <i>dayOfWeek</i> and convert it into a string.
 *     </li>
 *     <li>
 *         Default formatters - maintains a map between an object type and its default formatter.
 *         For example:<br>
 *         <i>Today is ${someDateVariable}</i><br>
 *         Will cause the message to detect the type of <i>someDateVariable</i> and fetch the relevant formatter.
 *     </li>
 * </ul>
 * </p>
 * A {@code Message} requires a locale and zone to be fully rendered.
 * The locale and zone are used to format numbers
 * and dates.<br>
 * A message factory serves as some sort of parent to its {@code Message}s. The factory maintains a locale
 * and a zone identifier that serves a message during its rendering stage.
 *
 * @author Guy Raz Nir
 * @since 2024/09/30
 */
public class MessageFactory {

    /**
     * Default locale to use when non-other specified.
     */
    public static final Locale DEFAULT_LOCALE = Locale.ENGLISH;

    /**
     * Default zone.
     */
    public static final ZoneId DEFAULT_ZONE = ZoneId.of("UTC");

    private MessageRenderContext messageRenderContext;

    /**
     * String fragmentator to use to parse messages templaes.
     */
    @SuppressWarnings("SpellCheckingInspection")
    private final StringFragmentator fragmentator = new StringFragmentator();

    /**
     * Maintains all available formatters and their function names.
     */
    private final Map<String, FormatterFactory<?>> namedFormatters = new HashMap<>();

    /**
     * Maintain a collection of default mapping between a value type and its formatter.
     */
    private final Map<Class<?>, Formatter> defaultFormatters = new HashMap<>();

    /**
     * Class constructor.
     */
    public MessageFactory() {
        this(null);
    }

    /**
     * Class constructor.
     *
     * @param context Message render context to use. If this value is {@code null}, a default one is created.
     */
    public MessageFactory(MessageRenderContext context) throws IllegalArgumentException {
        this.messageRenderContext = context != null ? context : new MessageRenderContext(DEFAULT_LOCALE, DEFAULT_ZONE);
    }

    /**
     * Creates a new message factory with default formatters (see {@link #createDefault(Locale, ZoneId)} for a list of
     * formatters), {@link #DEFAULT_LOCALE default locale}, {@link #DEFAULT_ZONE default zone}.
     *
     * @return New factory.
     */
    public static MessageFactory createDefault() {
        return createDefault(null, null);
    }

    /**
     * Creates a new message factory with default formatters, {@link #DEFAULT_ZONE default zone} and a given
     * <i>locale</i>.
     *
     * @param locale Locale to use.
     * @return New factory.
     */
    public static MessageFactory createDefault(Locale locale) {
        return createDefault(locale, null);
    }

    /**
     * Creates a new message factory with default formatters with {@link #DEFAULT_LOCALE default locale} and a given
     * zone.
     *
     * @param zoneId Zone to use.
     * @return New factory.
     */
    public static MessageFactory createDefault(ZoneId zoneId) {
        return createDefault(null, zoneId);
    }

    /**
     * A factory for creating a new {@code MessageFactory} with given <i>locale</i>, <i>zoneId</i> and a set of
     * default formatters for types such as {@code Byte}, {@code Short}, {@code Integer}, {@code Long}, {@code Date},
     * {@code LocalDate}, {@code Instant}, and others.
     *
     * @param locale Locale to use. If {@code null}, use {@link #DEFAULT_LOCALE default locale}.
     * @param zoneId Zone to use. If {@code null}, use {@link #DEFAULT_ZONE default zone}.
     * @return New factory.
     */
    public static MessageFactory createDefault(Locale locale, ZoneId zoneId) throws IllegalArgumentException {
        MessageFactory factory = new MessageFactory(new MessageRenderContext(locale != null ? locale : DEFAULT_LOCALE,
                zoneId != null ? zoneId : DEFAULT_ZONE));
        populateFactory(factory);

        return factory;
    }

    /**
     * Register a new function with its formatter.
     *
     * @param formatterName Name of formatter (e.g.: <i>numeric</i>, <i>percentage</i>, <i>dayOfWeek</i>,
     * @param factory       The factory that creates the actual formatter.
     * @throws IllegalArgumentException If either arguments are {@code null}.
     */
    public void registerNamedFormatter(String formatterName, FormatterFactory<?> factory) throws IllegalArgumentException {
        Asserts.notNull(formatterName, "Formatter name cannot be null.");
        Asserts.notNull(factory, "Formatter factory cannot be null.");

        namedFormatters.put(formatterName, factory);
    }

    /**
     * Register a default formatter for a given type.
     *
     * @param clazz     Type of object.
     * @param formatter Default formatter for the type.
     * @throws IllegalArgumentException If either arguments are {@code null}.
     */
    public void registerDefaultFormatter(Class<?> clazz, Formatter formatter) {
        Asserts.notNull(clazz, "Class cannot be null.");
        Asserts.notNull(formatter, "Formatter cannot be null.");

        defaultFormatters.put(clazz, formatter);
    }

    /**
     * @return Message render context.
     */
    public MessageRenderContext getMessageRenderContext() {
        return messageRenderContext;
    }

    /**
     * Sets a new message render context.
     *
     * @param messageRenderContext Context to set.
     * @throws IllegalArgumentException If <i>messageRenderContext</i> is {@code null}.
     */
    public void setMessageRenderContext(MessageRenderContext messageRenderContext) {
        Asserts.notNull(messageRenderContext, "Context cannot be null.");
        this.messageRenderContext = messageRenderContext;
    }

    /**
     * Compile a message template into a {@code Message}.
     *
     * @param message Message to compile.
     * @return A new {@code Message} object.
     * @throws IllegalArgumentException If <i>message</i> is {@code null}.
     * @throws StringFragmentsException If the provided message contains a malformed message format.
     */
    public Message compile(String message) throws IllegalArgumentException, StringFragmentsException {
        Asserts.notNull(message, "Message cannot be null.");
        Fragments fragments = fragmentator.parsePattern(message);
        final TokenDefinitionParser parser = new TokenDefinitionParser();

        //
        // Convert fragments into message construct, where each constructs.
        //
        List<MessageConstruct> messageConstructs = new LinkedList<>();
        fragments.visit(new FragmentsVisitor() {

            @Override
            public void textFragment(TextFragment fragment) {
                messageConstructs.add(new TextMessageConstruct(fragment.contents));
            }

            @Override
            public void tokenFragment(TokenFragment fragment) {
                TokenDefinitionParser.TokenDefinition definition = parser.parse(fragment.contents);
                MessageConstruct construct;

                //
                // If we encountered an explicit token definition, we need to create a specific formatter.
                // Otherwise -- we need to create a construct that selects the right formatter during render time.
                //
                if (definition.isExplicit()) {
                    FormatterFactory<?> factory = namedFormatters.get(definition.formatterName());
                    if (factory == null) {
                        throw new FormatErrorException("Unknown formatter: %s (variable name: %s)."
                                .formatted(definition.formatterName(), definition.variableName()));
                    }
                    Formatter formatter = factory.createFormatter(definition.properties());
                    construct = new FormatterVariableConstruct(definition.variableName(), formatter);
                } else {
                    construct = new DynamicFormatterVariableConstruct(fragment.contents,
                            new HashMap<>(defaultFormatters));
                }

                messageConstructs.add(construct);
            }
        });

        return new Message(messageRenderContext, message, messageConstructs);
    }

    /**
     * Populate a given <i>factory</i> with default formatters per type. Supported types are: {@link java.lang.Byte},
     * {@link Short}, {@link Integer}, {@link Long}, {@link AtomicInteger}, {@link AtomicLong}, {@link BigInteger},
     * {@link Float}, {@link Double}, {@link BigDecimal}, {@link Date}, {@link LocalDate} and {@link Instant}.
     *
     * @param factory Factory to populate.
     */
    private static void populateFactory(MessageFactory factory) {
        //
        // Integer values formatter.
        //
        IntegerFormatter integerFormatter = new IntegerFormatterFactory().createFormatter(Map.of());
        factory.registerDefaultFormatter(Byte.class, integerFormatter);
        factory.registerDefaultFormatter(Short.class, integerFormatter);
        factory.registerDefaultFormatter(Integer.class, integerFormatter);
        factory.registerDefaultFormatter(Long.class, integerFormatter);
        factory.registerDefaultFormatter(AtomicInteger.class, integerFormatter);
        factory.registerDefaultFormatter(AtomicLong.class, integerFormatter);
        factory.registerDefaultFormatter(BigInteger.class, integerFormatter);

        //
        // Decimal values formatter.
        //
        DecimalFormatter decimalFormatter = new DecimalFormatterFactory().createFormatter();
        factory.registerDefaultFormatter(Float.class, decimalFormatter);
        factory.registerDefaultFormatter(Double.class, decimalFormatter);
        factory.registerDefaultFormatter(BigDecimal.class, decimalFormatter);

        DateFormatter dateFormatter = new DateFormatterFactory().createFormatter();
        factory.registerDefaultFormatter(Date.class, dateFormatter);
        factory.registerDefaultFormatter(LocalDate.class, dateFormatter);
        factory.registerDefaultFormatter(Instant.class, dateFormatter);

        //
        // String, StringBuilder and StringBuffer formatter.
        //
        StringFormatter stringFormatter = new StringFormatterFactory().createFormatter();
        factory.registerDefaultFormatter(String.class, stringFormatter);
        factory.registerDefaultFormatter(StringBuilder.class, stringFormatter);
        factory.registerDefaultFormatter(StringBuffer.class, stringFormatter);

        factory.registerNamedFormatter("string", new StringFormatterFactory());
        factory.registerNamedFormatter("integer", new IntegerFormatterFactory());
        factory.registerNamedFormatter("decimal", new DecimalFormatterFactory());
        factory.registerNamedFormatter("percentage", new PercentageFormatterFactory());
        factory.registerNamedFormatter("date", new DateFormatterFactory());
        factory.registerNamedFormatter("time", new TimeFormatterFactory());
        factory.registerNamedFormatter("currency", new CurrencyFormatterFactory());
    }

}
