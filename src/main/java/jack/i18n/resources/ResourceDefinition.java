package jack.i18n.resources;


/**
 * Defines a resource properties, such as type and inheritable options.
 *
 * @param resourceName Name of resource.
 * @param resourceType Type of resource.
 * @param inheritable  Define if the resource can be inherited from a parent (if any) or must be declared for all
 *                     resource bundles, regardless if the bundle has a parent or not.
 *                     {@code true} if this resource can be inherited from parent resource, {@code false} if each
 *                     resource bundle must declare this resource locally (cannot be inherited from a parent, even if
 *                     parent exists).
 * @author Guy Raz Nir
 * @since 2024/07/28
 */
public record ResourceDefinition(String resourceName, Class<?> resourceType, boolean inheritable) {
}
