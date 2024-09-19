package jack.utils;

/**
 * Typical size units with magnitude (size) and suffix (e.g.: "K" for Kilo, "M" for "Mega", ...).
 *
 * @author Guy Raz Nir
 * @since 2024/09/18
 */
enum SizeUnit {

    BYTES(1L, ""),
    KB(1024L, "K"),
    MB(1024L * 1024L, "M"),
    GB(1024L * 1024L * 1024L, "G"),
    TB(1024L * 1024L * 1024L * 1024L, "T"),
    PB(1024L * 1024L * 1024L * 1024L * 1024L, "P"),
    EB(1024L * 1024L * 1024L * 1024L * 1024L * 1024L, "E");

    /**
     * No. of bytes per this unit.
     */
    public final long magnitude;

    /**
     * Unit suffix.
     */
    public final String suffix;

    /**
     * Class constructor.
     *
     * @param magnitude Number of bytes per unit.
     */
    SizeUnit(long magnitude, String suffix) {
        this.magnitude = magnitude;
        this.suffix = suffix;
    }
}
