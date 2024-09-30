package jack.i18n.messages;

import jack.i18n.messages.formatters.FormatErrorException;
import org.assertj.core.api.AssertionsForInterfaceTypes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static java.util.Map.entry;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

/**
 * Test cases for {@link TokenDefinitionParser}.
 *
 * @author Guy Raz Nir
 * @since 2024/10/19
 */
public class TokenDefinitionParserTest {

    /**
     * Instance to run tests on.
     */
    private static final TokenDefinitionParser parser = new TokenDefinitionParser();

    /**
     * Test parsing of a vague token.
     */
    @Test
    @DisplayName("Test should parse vague token.")
    public void testShouldParseVagueToken() {
        TokenDefinitionParser.TokenDefinition definition = parser.parse("firstName");
        assertThat(definition.variableName()).isEqualTo("firstName");
        assertThat(definition.isExplicit()).isFalse();
    }

    /**
     * Test should parse explicit token with formatter name.
     */
    @Test
    @DisplayName("Test should parse explicit token")
    public void testShouldParseExplicitToken() {
        TokenDefinitionParser.TokenDefinition definition = parser.parse("age;decimal");
        assertThat(definition.variableName()).isEqualTo("age");
        assertThat(definition.formatterName()).isEqualTo("decimal");
    }

    /**
     * Test should parse properties.
     */
    @Test
    @DisplayName("Test should parse properties")
    public void testShouldParseTokenProperties() {
        TokenDefinitionParser.TokenDefinition definition = parser.parse("age;decimal;decimalPoints=2;padding=true");
        AssertionsForInterfaceTypes
                .assertThat(definition.properties())
                .contains(entry("decimalPoints", "2"))
                .contains(entry("padding", "true"));
    }

    /**
     * Test should fail on missing formatter on an explicit token.
     */
    @Test
    @DisplayName("Test should fail on missing formatter name")
    public void testShouldFailOnMissingFormatter() {
        assertThatExceptionOfType(FormatErrorException.class).isThrownBy(() -> parser.parse("age;;decimalPoints=2;padding=true"));
    }

    /**
     * Test should fail on empty (zero-string length) variable name.
     */
    @Test
    @DisplayName("Test should fail on missing variable name")
    public void testShouldFailOnMissingVariableName() {
        assertThatExceptionOfType(FormatErrorException.class).isThrownBy(() -> parser.parse("   ;decimal;decimalPoints=2;padding=true"));
    }

    /**
     * Test should fail on invalid property definition.
     */
    @Test
    @DisplayName("Test should fail on invalid property format")
    public void testShouldFailOnInvalidPropertyFormat() {
        assertThatExceptionOfType(FormatErrorException.class).isThrownBy(() -> parser.parse(";decimal;decimalPoints"));
    }

}
