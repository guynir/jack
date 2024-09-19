package jack.idgen;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * An identifier generator which runs sequentially starting at a given number.<p>
 * </p>
 * The implementation is thread-safe and can be called concurrently.
 *
 * @author Guy Raz Nir
 * @since 2024/09/17
 */
public class SequencialIntegerIdGenerator implements IdGenerator<Integer> {

    /**
     * Internal counter.
     */
    private final AtomicInteger counter = new AtomicInteger();

    /**
     * Class constructor.
     */
    public SequencialIntegerIdGenerator() {
    }

    /**
     * Class constructor.
     *
     * @param initialValue Initial value to start with.
     */
    public SequencialIntegerIdGenerator(int initialValue) {
        counter.set(initialValue);
    }

    /**
     * @return Next integer identifier.
     */
    @Override
    public Integer generate() {
        return counter.getAndIncrement();
    }
}
