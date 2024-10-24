package jack.i18n.resources;

import jack.utils.Asserts;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * This class provides building facilities for {@link ResourceBundlePack}. It provides an easy API for accumulating
 * resources while aggregating by locale.<p>
 * </p>
 * The builder also performs validation on the data, ensuring all data is valid.
 *
 * @author Guy Raz Nir
 * @since 2024/07/28
 */
public class ResourceBundlePackBuilder {

    /**
     * A cached list producer for {@link #registerResource(String, Class, boolean)}.
     */
    private static final Function<String, List<ResourceDefinition>> NEW_LIST_PRODUCER = k -> new LinkedList<>();
    /**
     * A cached map producer for {@link #getBundleMap(String)}.
     */
    private static final Function<Locale, Map<String, Object>> NEW_MAP_PRODUCER = k -> new HashMap<>();
    /**
     * A set of ISO-631 2-character language codes (e.g., <i>en</i>, <i>fr</i>, <i>iw</i>, ...).
     */
    private static final Set<String> ISO_631_1_LANG_CODES = new HashSet<>(Arrays.asList(Locale.getISOLanguages()));
    /**
     * Maximum number of errors to include in an exception message.
     */
    private static final int MAX_MESSAGES_IN_EXCEPTION = 4;
    /**
     * Holds the list of resource definitions.
     */
    private final Map<String, List<ResourceDefinition>> definitions = new HashMap<>();
    /**
     * Strategy to use for building hierarchy between different locales.
     */
    private final HierarchyStrategy hierarchyStrategy;
    /**
     * Contains all resources aggregated by locale.
     */
    private final Map<Locale, Map<String, Object>> bundlesMap = new HashMap<>();
    /**
     * Default locale.
     */
    private String defaultLanguage = null;

    /**
     * Class constructor.
     *
     * @param strategy Strategy to use for building locales' hierarchy.
     */
    private ResourceBundlePackBuilder(HierarchyStrategy strategy) {
        this.hierarchyStrategy = strategy;
    }

    /**
     * @return A new builder.
     */
    public static ResourceBundlePackBuilder newBuilder() {
        return new ResourceBundlePackBuilder(new LocaleBasedHierarchyStrategy());
    }

    /**
     * Sets the default language to use. This will determine the order and relationship between locales.
     *
     * @param defaultLanguage Default language to use.
     * @throws IllegalArgumentException If either <i>defaultLanguage</i> contains {@code null} or empty-strings
     *                                  or the language code is not valid (does not match any of ISO-631-1 codes).
     */
    public ResourceBundlePackBuilder setDefaultLanguage(String defaultLanguage) throws IllegalArgumentException {
        Asserts.notEmpty(defaultLanguage, "Default language is undefined (empty or null).");
        if (!ISO_631_1_LANG_CODES.contains(defaultLanguage)) {
            throw new IllegalArgumentException("Invalid language code: " + defaultLanguage);
        }

        this.defaultLanguage = defaultLanguage;
        return this;
    }

    /**
     * Register a new inheritable resource definition. Besides asserting that all arguments are non-{@code null}, no
     * validation is performed.
     *
     * @param resourceKey  Resource key.
     * @param resourceType Resource Java type.
     * @throws IllegalArgumentException If either <i>resourceKey</i> or <i>resourceType</i> are {@code null}.
     */
    public ResourceBundlePackBuilder registerResource(String resourceKey,
                                                      Class<?> resourceType) throws IllegalArgumentException {
        return registerResource(resourceKey, resourceType, true);
    }

    /**
     * Register a new resource definition. Besides asserting that all arguments are non-{@code null}, no validation
     * is performed.
     *
     * @param resourceKey  Resource key.
     * @param resourceType Resource Java type.
     * @param inheritable  {@code true} if this resource can be inherited from parent resource, if available or
     *                     {@code false} if each resource bundle must declare this resource locally (cannot be inherited
     *                     from a parent, even if parent exists).
     * @throws IllegalArgumentException If either <i>resourceKey</i> or <i>resourceType</i> are {@code null}.
     */
    public ResourceBundlePackBuilder registerResource(String resourceKey,
                                                      Class<?> resourceType,
                                                      boolean inheritable) throws IllegalArgumentException {
        Asserts.notEmpty(resourceKey, "Resource key cannot be null or empty.");
        Asserts.notNull(resourceType, "Resource type cannot be null.");

        definitions.computeIfAbsent(resourceKey, NEW_LIST_PRODUCER)
                .add(new ResourceDefinition(resourceKey, resourceType, inheritable));

        return this;
    }

    /**
     * Add a resource to this builder under a given locale. No validation is performed at this stage on either the
     * resource key or value.
     *
     * @param localeTag     A tag of locale to which the resource is associated with.
     * @param resourceKey   Resource key.
     * @param resourceValue Resource value.
     * @throws IllegalArgumentException If either arguments are {@code null}.
     */
    public ResourceBundlePackBuilder addResource(String localeTag,
                                                 String resourceKey,
                                                 Object resourceValue) {
        Asserts.notEmpty(resourceKey, "Resource key cannot be null or empty.");
        Asserts.notNull(resourceValue, "Resource value cannot be null.");

        getBundleMap(localeTag).put(resourceKey, resourceValue);
        return this;
    }

    /**
     * Perform validation on all the data using the following rules:
     * <ul>
     *     <li>
     *         A default language is defined.
     *     </li>
     *     <li>
     *         A resource bundle for the default language exists.
     *     </li>
     *     <li>
     *         Validate that all resources are registered (via {@link #registerResource(String, Class, boolean)})
     *         and are compliant with their type.
     *     </li>
     * </ul>
     *
     * @return List of errors found or empty list if no error was found.
     */
    public List<String> validate() {
        List<String> errors = new LinkedList<>();

        //
        // Check the definition of the default locale.
        //
        if (defaultLanguage == null) {
            // We're missing the default language.
            errors.add("Default language is not specified.");
        } else if (!bundlesMap.containsKey(new Locale(defaultLanguage))) {
            // Make sure default language locale defined in our bundle.
            errors.add("Language " + defaultLanguage + " has no resource bundle associated with it.");
        }

        //
        // Check that for each definition is defined only once.
        //
        Set<String> nonInheritableResources = new HashSet<>();
        definitions.forEach((key, value) -> {
            if (value.size() > 1) {
                errors.add("Resource '%s' has duplicate (%d instances) definitions.".formatted(key, value.size()));
            }

            // We also build a list of non-inheritable resources, to perform validation with these names later on.
            value.forEach(def -> {
                if (!def.inheritable()) {
                    nonInheritableResources.add(key);
                }
            });
        });

        // Create a list of all parent locales (those who has no parent).
        Set<Locale> parentLocales = createChildParentMap()
                .entrySet()
                .stream()
                .filter(e -> e.getValue() == null)
                .map(Map.Entry::getKey)
                .collect(Collectors.toSet());

        //
        // Make sure that all resources are defined as their registered type.
        //
        bundlesMap.forEach((locale, map) -> {

            Set<String> resourceKeys = parentLocales.contains(locale) ?
                    new HashSet<>(definitions.keySet()) :
                    new HashSet<>();
            map.forEach((key, value) -> {
                resourceKeys.remove(key);

                List<ResourceDefinition> definition = definitions.get(key);
                if (definition == null) {
                    errors.add("No definition found for resource '%s' (locale: %s).".formatted(key, locale));
                } else {
                    //
                    // Having multiple definitions for a resource is an error; however, we will try to evaluate
                    // if any of the definition matches the current key - it is sufficed.
                    // Otherwise -- if no match found -- register an error.
                    //
                    boolean match = false;
                    for (ResourceDefinition resourceDefinition : definition) {
                        match = definition.get(0).resourceType().equals(value.getClass());
                        if (match) {
                            break;
                        }
                    }

                    if (!match) {
                        errors.add("Resource '%s' (locale: %s) is not of expected type '%s' (actual type: '%s')."
                                .formatted(key,
                                        locale,
                                        definition.get(0).resourceType().getName(),
                                        value.getClass().getName()));
                    }
                }
            });

            // Make sure the current bundle has all non-inheritable resources defined.
            nonInheritableResources.forEach(resourceKey -> {
                if (!map.containsKey(resourceKey)) {
                    errors.add("Locale '%s' is missing a definition of resource '%s' (resource is not inheritable).".
                            formatted(locale, resourceKey));
                }
            });

            //
            // We are inspecting a parent locale that not all of its resources are defined.
            //
            if (!resourceKeys.isEmpty()) {
                resourceKeys.forEach(key ->
                        errors.add("Resource '%s' is missing at locale '%s' (a parent locale).".formatted(key, locale)));
            }
        });

        return errors;
    }

    /**
     * Build a resource pack based on the configuration and resources set in this builder.
     *
     * @return A new resource pack.
     * @throws ResourceBundleValidationException If pack validation failed. The message will include some errors.
     */
    public ResourceBundlePack build() throws ResourceBundleValidationException {
        //
        // Validate that all data is valid.
        //
        List<String> errors = validate();
        if (!errors.isEmpty()) {
            //
            // Generate an error message that includes, at most 3 items.
            //
            StringBuilder builder = new StringBuilder("Resource bundle errors.\n");

            for (int i = 0; i < Math.min(errors.size(), MAX_MESSAGES_IN_EXCEPTION); i++) {
                builder.append("\t");
                builder.append(errors.get(i));
                builder.append("\n");
            }

            if (errors.size() > MAX_MESSAGES_IN_EXCEPTION) {
                builder.append("\t.... (additional ")
                        .append(errors.size() - MAX_MESSAGES_IN_EXCEPTION)
                        .append(" errors).\n");
            }

            throw new ResourceBundleValidationException(builder.toString(), errors);
        }

        //
        // Convert a definition map resourceKey->List of definitions to simple resourceKey->Definition.
        //
        Map<String, ResourceDefinition> definitionMap = definitions
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, v -> v.getValue().get(0)));

        // Create a child/parent relationship. Each child locale (e.g.: 'en_US') points to its parent locale
        // (e.g.: 'en).
        Map<Locale, Locale> hierarchy = createChildParentMap();

        // Order the locales in such a way that root locales appear first followed by children locales.
        List<Locale> localeOrder = LocaleUtils.orderLocalesByReference(hierarchy);

        // Target map to hold bundles.
        Map<Locale, ResourceBundle> bundles = new HashMap<>(bundlesMap.size());

        //
        // Iterate over all available locales in the given order and create ResourceBundles for each locale.
        //
        localeOrder.forEach((locale) -> {
            Locale parentLocale = hierarchy.get(locale);
            ResourceBundle parent = bundles.get(parentLocale);

            // Just a validity check that everything is OK.
            // There should never be a case where a parent locale is not available.
            if (parentLocale != null && parent == null) {
                throw new IllegalStateException("Unexpected error: Could not find parent locale "
                        + parentLocale
                        + " for locale "
                        + locale
                        + ".");
            }

            ResourceBundle newBundle = new ResourceBundle(locale, parent, bundlesMap.get(locale));
            bundles.put(locale, newBundle);
        });

        Locale defaultLocale = new Locale(defaultLanguage);
        return new ResourceBundlePack(bundles, defaultLocale);
    }

    /**
     * Get (or create on-demand) a resource map for a given locale.
     *
     * @param localeTag Locale tag to look by.
     * @return A resource map for the given locale.
     * @throws IllegalArgumentException   If <i>locale</i> is either {@code null} or {@code empty}.
     * @throws UnsupportedLocaleException If provided <i>locale</i> is unsupported/invalid locale tag.
     */
    protected Map<String, Object> getBundleMap(String localeTag) throws IllegalArgumentException, UnsupportedLocaleException {
        return bundlesMap.computeIfAbsent(LocaleUtils.parseLocale(localeTag), NEW_MAP_PRODUCER);
    }

    /**
     * Creates a relationship map between children and their parents. Future implementations may change this function's
     * behavior.
     *
     * @return A map between a child locale and its parent locale.
     */
    protected Map<Locale, Locale> createChildParentMap() {
        return hierarchyStrategy.getRelationships(bundlesMap.keySet());
    }
}
