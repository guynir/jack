package jack.strings;

public interface FragmentsVisitor {

    void textFragment(TextFragment fragment);

    void tokenFragment(TokenFragment fragment);
}
