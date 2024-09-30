package jack.i18n.messages.formatters;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.ZoneId;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.catchThrowableOfType;

/**
 * Test cases for {@link ContextAwareVariableFormatter}.
 *
 * @author Guy Raz Nir
 * @since 2024/10/09
 */
public class ContextAwareVariableFormatterTest {

    /**
     * Sample locale to use for testing.
     */
    private static final Locale LOCALE = Locale.ENGLISH;

    /**
     * Sample zone to use for testing.
     */
    private static final ZoneId ZONE_ID = ZoneId.systemDefault();
    /**
     * An integer/numeric formatter to use for testing.
     */
    private static final Formatter FORMATTER = new IntegerFormatter();
    /**
     * Name of sample variable.
     */
    private static final String VARIABLE_NAME = "age";
    /**
     * Integer value of sample variable.
     */
    private static final Integer VARIABLE_VALUE = 18;
    /**
     * String representation of sample variable value.
     */
    private static final String VARIABLE_VALUE_STR = Integer.toString(VARIABLE_VALUE);
    /**
     * Formatter to test. A new instance is created for each test.
     */
    private ContextAwareVariableFormatter formatter;
    /**
     * Context to use for testing. A new instance is created for each test.
     */
    private Map<String, Object> context;

    /**
     * Text fixture -- prepare a {@link #formatter} and {@link #context} before each
     * test is executed.
     */
    @BeforeEach
    public void setUp() {
        context = new HashMap<>();
        formatter = new ContextAwareVariableFormatter(FORMATTER, VARIABLE_NAME);
    }

    /**
     * Test that validation completes successfully when the variable exists in context and its type is supported by
     * the associated formatter.
     */
    @Test
    @DisplayName("Test should validate variable successfully")
    public void testShouldSuccessfullyValidateVariable() {
        context.put(VARIABLE_NAME, VARIABLE_VALUE);

        // Validation should be successful and not raise any error.
        formatter.validateVariable(context);
    }

    /**
     * Test should fail when a variable does not exist in context.
     */
    @Test
    @DisplayName("Test should fail on missing variable")
    public void testShouldFailOnMissingVariable() {
        // Catch the expected exception so we can inspect its 'errorType' property.
        VariableFormatErrorException ex = catchThrowableOfType(VariableFormatErrorException.class,
                () -> formatter.validateVariable(Map.of()));

        // We expect the error type to report the variable as missing.
        assertThat(ex.getErrorType()).isEqualTo(VariableFormatErrorException.VariableFormatErrorType.VARIABLE_UNDEFINED);
    }

    /**
     * Test should fail on a variable type which is not supported by the formatter.
     */
    @Test
    @DisplayName("Test should fail on unsupported variable type")
    public void testShouldFailOnUnsupportedVariableType() {
        // Catch the expected exception so we can inspect its 'errorType' property.
        VariableFormatErrorException ex = catchThrowableOfType(VariableFormatErrorException.class,
                () -> formatter.validateVariable(Map.of(VARIABLE_NAME, VARIABLE_VALUE_STR)));

        // We expect the error type to report the variable as missing.
        assertThat(ex.getErrorType()).isEqualTo(VariableFormatErrorException.VariableFormatErrorType.VARIABLE_TYPE_ERROR);
    }

    /**
     * Test should extract a variable from context and format it.
     */
    @Test
    @DisplayName("Test should format value")
    public void testShouldFormatIntegerValue() {
        context.put(VARIABLE_NAME, VARIABLE_VALUE);

        assertThat(formatter.format(LOCALE, ZONE_ID, context)).isEqualTo(VARIABLE_VALUE_STR);
    }
}
