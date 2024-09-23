package jack.strings;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * A collection of test cases for {@link StringFragmentator}.
 *
 * @author Guy Raz Nir
 * @since 2024/08/10
 */
public class TestFragmentatorTest {

    /**
     * Test should parse a string as a single text block.
     */
    @Test
    @DisplayName("Test should produce text only part")
    public void testShouldProduceTextOnlyPart() {
        // Sample text without a token.
        final String TEXT = "This is a sample text !";
        Fragments fragments = StringFragmentator.parse(TEXT);

        assertThat(fragments.fragments().size()).isEqualTo(1);
        assertThat(fragments.fragments().get(0)).isEqualTo(new TextFragment(TEXT, 0, TEXT.length()));
    }

    /**
     * Test should parse simple text and tokens from a given text.
     */
    @Test
    @DisplayName("Test should parse text and tokens parts from text")
    public void testShouldParseTokensFromText() {
        // Sample text.
        final String TEXT = "Today is ${nameOfDay}; tomorrow is ${nameOfTomorrow}.";

        List<Fragment> expectedTokens = FragmentsBuilder.newBuilder()
                .addText("Today is ")
                .addToken("${nameOfDay}")
                .addText("; tomorrow is ")
                .addToken("${nameOfTomorrow}")
                .addText(".")
                .toList();

        Fragments fragments = StringFragmentator.parse(TEXT);

        assertThat(fragments.fragments().size()).isEqualTo(expectedTokens.size());
    }

    /**
     * Test that a string composed of single token fragment is detected correctly.
     */
    @Test
    @DisplayName("Test should find a single token fragment")
    public void testShouldFindSingleTokenFragment() {
        final String TEXT = "${name}";
        Fragments fragments = StringFragmentator.parse(TEXT);

        TokenFragment expectedToken = new TokenFragment("name", 0, TEXT.length() - 1);

        // We are expecting a single token fragment representing the entire original text.
        assertThat(fragments.fragments()).isEqualTo(List.of(expectedToken));
    }

}
