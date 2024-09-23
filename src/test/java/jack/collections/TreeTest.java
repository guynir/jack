package jack.collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * A collection of test cases for {@link Tree} and {@link TreeNode}.
 *
 * @author Guy Raz Nir
 * @since 2024/09/23
 */
public class TreeTest {

    /**
     * Test that for a given key path a node is located.
     */
    @Test
    @DisplayName("Test should find node")
    public void testShouldFindNode() {
        Tree<String, String> tree = new Tree<>();
        TreeNode<String, String> nodeA = tree.getRoot().add("1", "A");
        TreeNode<String, String> nodeB = tree.getRoot().add("2", "B");
        TreeNode<String, String> nodeC = tree.getRoot().add("3", "C");

        nodeA.add("11", "AA");

        assertThat(tree.getNode("1", "11")).isNotNull();
        assertThat(tree.getNode("1", "11").getData()).isEqualTo("AA");
    }

    /**
     * Test that given a path that resolves into a non-existing node, the returned value of a search yields
     * {@code null}.
     */
    @Test
    @DisplayName("Test should not find a node")
    public void testShouldNotFindNode() {
        Tree<String, String> tree = new Tree<>();
        TreeNode<String, String> nodeA = tree.getRoot().add("1", "A");

        assertThat(tree.getNode("1", "11")).isNull();
    }

    /**
     * Test should create all missing nodes in a tree.
     */
    @Test
    @DisplayName("Test should create missing nodes")
    public void testShouldCreateMissingNodes() {
        Tree<String, String> tree = new Tree<>();
        TreeNode<String, String> finalNode = tree.getOrCreateNode("A", "B", "C");

        assertThat(tree.getNode("A", "B", "C")).isNotNull();
    }
}
