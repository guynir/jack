package jack.i18n.resources;

import jack.utils.Asserts;

import java.time.LocalDateTime;
import java.util.Locale;
import java.util.Map;

/**
 * A resource bundle contains all (or most) of the resources required for a given locale. A resource can be
 * a simple data type (e.g., {@code java.lang.Long}, {@code java.lang.Boolean}, {@code java.lang.Double},
 * {@code java.lang.String}) or a very specific type -- {@code Message message object}.
 *
 * @author Guy Raz Nir
 * @since 2024/07/31
 */
public class ResourceBundle {

    /**
     * The locale of this bundle.
     */
    private final Locale locale;

    /**
     * A parent bundle to delegate call to if this resource bundle does not contain the resource.
     */
    private final ResourceBundle parent;

    /**
     * Collection of resources for this bundle.
     */
    private final Map<String, Object> resourcesMap;

    /**
     * Class constructor.
     *
     * @param locale       The locale of this bundle.
     * @param parent       Parent locale to delegate call to if resource does not exist in this bundle.
     * @param resourcesMap Resources.
     */
    ResourceBundle(Locale locale, ResourceBundle parent, Map<String, Object> resourcesMap) {
        this.locale = locale;
        this.parent = parent;
        this.resourcesMap = resourcesMap;
    }

    /**
     * @return The locale of this bundle.
     */
    public Locale getLocale() {
        return locale;
    }

    /**
     * @return The parent resource bundle, if any or {@code null} if this bundle has no parent.
     */
    public ResourceBundle getParent() {
        return parent;
    }

    /**
     * @return {@code true} if this bundle has a parent, {@code false} if not.
     */
    public boolean hasParent() {
        return parent != null;
    }

    /**
     * Fetch a resource by its key. If resource does not exist locally, it is inheritable, and this bundle has a
     * parent, the parent is requested to resolve the key.
     *
     * @param key A key denoting a resource.
     * @return A resource value.
     * @throws UnknownResourceException If resource could not be resolved.
     */
    public Object get(String key) throws UnknownResourceException {
        Asserts.notNull(key, "Resource key cannot be null.");
        Object value = resourcesMap.get(key);

        // If our local bundle does not have the requested key, try our parent.
        if (value == null) {
            if (parent != null) {
                try {
                    value = parent.get(key);
                } catch (UnknownResourceException ex) {
                    // In case our parent did not find the resource, we would like to through an exception
                    // on our own. No rely on the parent's exception.
                    throw new UnknownResourceException("Resource not available : " + key + " (locale: " + locale + ").",
                            ex);
                }
            } else {
                throw new UnknownResourceException("Resource not available: " + key);
            }
        }

        return value;
    }

    /**
     * Fetch a resource as a specific <i>type</i>.
     *
     * @param key  Resource key.
     * @param type Type of resource.
     * @param <T>  Generic type of resource.
     * @return Resource value.
     * @throws IllegalArgumentException If either <i>key</i> or <i>type</i> are {@code null}.
     * @throws UnknownResourceException If resource could not be found.
     * @throws ClassCastException       If resource could not be provided as the requested <i>type</i>.
     */
    public <T> T get(String key, Class<T> type)
            throws IllegalArgumentException, ClassCastException, UnknownResourceException {
        Asserts.notNull(type, "Resource type cannot be null.");

        Object value = get(key);

        T castedValue;
        if (type.isInstance(value)) {
            castedValue = type.cast(value);
        } else if (String.class.equals(type) && (value instanceof Number || value instanceof Boolean)) {
            //noinspection unchecked
            castedValue = (T) value.toString();
        } else {
            throw new ClassCastException("Cannot convert resource '%s' of type %s to %s."
                    .formatted(key, value.getClass().getName(), type.getName()));
        }

        return castedValue;
    }

    /**
     * Fetch a resource as a string.
     *
     * @param key Key denoting the resource.
     * @return Resource value.
     * @throws IllegalArgumentException If <i>key</i> is {@code null}.
     * @throws ClassCastException       If resource could not be provided as a string.
     * @throws UnknownResourceException If resource does not exist.
     */
    public String getAsString(String key)
            throws IllegalArgumentException, ClassCastException, UnknownResourceException {
        return get(key, String.class);
    }

    /**
     * Fetch a resource as an integer (log) number.
     *
     * @param key Key denoting the resource.
     * @return Resource value.
     * @throws IllegalArgumentException If <i>key</i> is {@code null}.
     * @throws ClassCastException       If resource could not be provided as a long number.
     * @throws UnknownResourceException If resource does not exist.
     */
    public Long getAsLong(String key)
            throws IllegalArgumentException, ClassCastException, UnknownResourceException {
        return get(key, Long.class);
    }

    /**
     * Fetch a resource as a decimal (double) number.
     *
     * @param key Key denoting the resource.
     * @return Resource value.
     * @throws IllegalArgumentException If <i>key</i> is {@code null}.
     * @throws ClassCastException       If resource could not be provided as a double number.
     * @throws UnknownResourceException If resource does not exist.
     */
    public Double getAsDouble(String key)
            throws IllegalArgumentException, ClassCastException, UnknownResourceException {
        return get(key, Double.class);
    }

    /**
     * Fetch a resource as a boolean value.
     *
     * @param key Key denoting the resource.
     * @return Resource value.
     * @throws IllegalArgumentException If <i>key</i> is {@code null}.
     * @throws ClassCastException       If resource could not be provided as a boolean value.
     * @throws UnknownResourceException If resource does not exist.
     */
    public Boolean getAsBoolean(String key)
            throws IllegalArgumentException, ClassCastException, UnknownResourceException {
        return get(key, Boolean.class);
    }

    /**
     * Fetch a resource as a date/time value.
     *
     * @param key Key denoting the resource.
     * @return Resource value.
     * @throws IllegalArgumentException If <i>key</i> is {@code null}.
     * @throws ClassCastException       If resource could not be provided as a date/time value.
     * @throws UnknownResourceException If resource does not exist.
     */
    public LocalDateTime getAsDateTime(String key)
            throws IllegalArgumentException, ClassCastException, UnknownResourceException {
        return get(key, LocalDateTime.class);
    }


}
