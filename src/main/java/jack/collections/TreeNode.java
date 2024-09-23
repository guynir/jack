package jack.collections;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Represents a single node in the tree. Each such node contains associated data (optional) and children (optional).
 *
 * @param <K> Generic type of node's key.
 * @param <D> Generic type of node's data.
 * @author Guy Raz Nir
 * @since 26/06/2017
 */
public class TreeNode<K, D> {

    /**
     * Data associated the current node.
     */
    private D data;

    /**
     * Children of this node.
     */
    private final Map<K, TreeNode<K, D>> children = new LinkedHashMap<>();

    /**
     * Class constructor.
     */
    public TreeNode() {
        this(null);
    }

    /**
     * Class constructor.
     *
     * @param data Optional data to associate with this node.
     */
    public TreeNode(D data) {
        this.data = data;
    }

    /**
     * @return Return associated data. May be {@code null}.
     */
    public D getData() {
        return data;
    }

    /**
     * Associate new data with this node.
     *
     * @param data New data to associate. May be {@code null}.
     */
    public void setData(D data) {
        this.data = data;
    }

    /**
     * Add a new child to this node.
     *
     * @param key  Key that identifies the child. If this key already exists, it is overridden.
     * @param data Data to set for the new node.
     * @return Newly created node.
     */
    public TreeNode<K, D> add(K key, D data) {
        TreeNode<K, D> newNode = new TreeNode<>(data);
        children.put(key, newNode);
        return newNode;
    }

    /**
     * Fetch node based on given <i>key</i>.
     *
     * @param key Key of node.
     * @return Node matching given <i>key</i> or {@code null} if no such key exists.
     */
    public TreeNode<K, D> getNode(K key) {
        return this.children.get(key);
    }

    /**
     * Fetch node given a path.
     *
     * @param path Path (set of keys) to target node.
     * @return Node matching given <i>path</i> or {@code null} if no such node exists.
     */
    @SafeVarargs
    public final TreeNode<K, D> getNode(K... path) {
        if (path.length == 0) {
            throw new IllegalArgumentException("Path requires to have at least one key.");
        }

        TreeNode<K, D> current = this;
        int index = 0;
        while (current != null && index < path.length) {
            current = current.getNode(path[index]);
            index++;
        }

        return current;
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
        if (path.length == 0) {
            throw new IllegalArgumentException("Path requires to have at least one key.");
        }

        TreeNode<K, D> node = this;
        for (K key : path) {
            TreeNode<K, D> child = node.getNode(key);
            if (child == null) {
                child = node.add(key, null);
            }
            node = child;
        }

        return node;
    }

    /**
     * Remove a node (with all of its children).
     *
     * @param key A node to remove.
     * @return {@code true} if node exists and is removed, {@code false} if node does not exist.
     */
    public boolean remove(K key) {
        return children.remove(key) != null;
    }

    /**
     * @return List of children's node data. Will exclude {@code null} values.
     */
    public List<D> getChildrenData() {
        return this.children.values().stream().map(TreeNode::getData).filter(Objects::nonNull).collect(Collectors.toList());
    }
}
