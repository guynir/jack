package jack.collections;

/**
 * <p>Implementation of tree data structure (parent with multiple children).
 * </p>
 * Each node has two core properties:
 * <ul>
 *     <li><i>key</i> - A key that represent it and is unique only among siblings (the same key can repeat in other
 *     group of children).</li>
 *     <li><i>data</i> - Each node carries a data object.</li>
 * </ul>
 * <p>It is important to note that this model is not thread-safe (for performance reasons). An underlying implementation
 * must take care of access control in a multithreaded environment.</p>
 *
 * @author Guy Raz Nir
 * @since 26/06/2017
 */
public class Tree<K, D> {

    /**
     * Root node of the tree.
     */
    private final TreeNode<K, D> rootNode = new TreeNode<>();

    /**
     * Class constructor.
     */
    public Tree() {
    }

    /**
     * Find a node based on path to it.
     *
     * @param path Set of keys denoting a Path to the target node.
     * @return Node denoted by <i>keyPath</i> or {@code null} no such node exists.
     */
    @SafeVarargs
    public final TreeNode<K, D> getNode(K... path) {
        return rootNode.getNode(path);
    }

    /**
     * Fetch a node denoted by a given path (set of keys). If nodes in the path do not exist during search, new
     * nodes are created with {@code null} data.
     *
     * @param path Path to node.
     * @return Node denoted by <i>path</i>. May return {@code null} if <i>path</i> is an empty array.
     */
    @SafeVarargs
    public final TreeNode<K, D> getOrCreateNode(K... path) {
        return rootNode.getOrCreateNode(path);
    }

    /**
     * @return Root node of this tree. Each tree is created with a root node.
     */
    public TreeNode<K, D> getRoot() {
        return rootNode;
    }

}
