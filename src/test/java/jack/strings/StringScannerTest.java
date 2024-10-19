package jack.strings;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test suite for {@link StringScanner}.
 *
 * @author Guy Raz Nir
 * @since 2024/08/10
 */
public class StringScannerTest {

    /**
     * Test that searching for a substring in a string (which is not escaped) can be found.
     */
    @Test
    @DisplayName("Test should find a substring in a string")
    public void testShouldFindSubstring() {
        final String TEXT = "Hello there Mr. {{name}} !!!";

        StringScanner finder = new StringScanner(TEXT);
        assertThat(finder.find("{{")).isEqualTo(TEXT.indexOf("{{"));
        assertThat(finder.find("}}")).isEqualTo(TEXT.indexOf("}}"));

        assertThat(finder.find("}}")).isNegative();
    }

    /**
     * Test that {@code SubstringFinder} skips escaped substring and continue to the next match.
     */
    @Test
    @DisplayName("Test should skip escaped substring")
    public void testShouldSkipEscapedMatch() {
        final String TEXT = "Please do not use \\{{ in your string, Mr. {{name}} !!!";

        // The second match is the one our finder should report.
        // The first match is escaped and therefore should be skipped.
        int firstMatch = TEXT.indexOf("{{");
        int secondMatch = TEXT.indexOf("{{", firstMatch + 1);

        StringScanner finder = new StringScanner(TEXT);
        assertThat(finder.find("{{")).isEqualTo(secondMatch);
    }

    /**
     * Test that when search reached the end of the string, the {@link StringScanner#consumed() finish} flag is set.
     */
    @Test
    @DisplayName("Test should identify finish state")
    public void testShouldIdentityFinishState() {
        final String TEXT = "This is a sample text !";

        StringScanner finder = new StringScanner(TEXT);

        // Assert that the finish flag is not set yet.
        assertThat(finder.consumed()).isFalse();

        // Try to find a non-existing substring.
        finder.find("Hi!");

        // Assert that the 'finished' flag is set, as the scan reached the end of the string.
        assertThat(finder.consumed()).isTrue();
    }

    /**
     * Test that a match on the first character.
     */
    @Test
    @DisplayName("Test should find a match at first character")
    public void testShouldFindMatchAtFirstCharacter() {
        int index = StringScanner.findSubstring("a", "a");
        assertThat(index).isEqualTo(0);

        index = StringScanner.findSubstring("Hello, world !!!", "H");
        assertThat(index).isEqualTo(0);
    }

    /**
     * Test that a match on the last character.
     */
    @Test
    @DisplayName("Test should find a match at the last character")
    public void testShouldFindMatchAtLastCharacter() {
        final String TEXT = "\"Hello, world !\"";
        int index = StringScanner.findSubstring(TEXT, "!");
        assertThat(index).isEqualTo(TEXT.length() - 2);

    }

    /**
     * Test a simple splitting process.
     */
    @Test
    @DisplayName("Test should split a string")
    public void testShouldSplitString() {
        String[] parts = StringScanner.split("A;B;C;D", ";");
        assertThat(parts).isEqualTo(new String[]{"A", "B", "C", "D"});
    }

    /**
     * Test that splitter skip an <i>escaped splitter</i> -- a splitter which is preceded by an escape character.
     */
    @Test
    @DisplayName("Test should skip an escaped splitting string")
    public void testShouldSkipEscapedSplitString() {
        String[] parts = StringScanner.split("A\\;B;C;D", ";");
        assertThat(parts).isEqualTo(new String[]{"A\\;B", "C", "D"});
    }

    /**
     * Test that for an empty input string, returned value is an empty array.
     */
    @Test
    @DisplayName("Test should return empty array")
    public void testShouldReturnEmptyArray() {
        String[] parts = StringScanner.split("", ";");
        assertThat(parts).isEmpty();
    }

    /**
     * Test that for a string with only splitting strings, the returned value is an array holding empty strings.
     */
    @Test
    @DisplayName("Test should generate empty strings.")
    public void testShouldGenerateEmptyStrings() {
        String[] parts = StringScanner.split(";;", ";");
        assertThat(parts).hasSize(3);
    }

}
