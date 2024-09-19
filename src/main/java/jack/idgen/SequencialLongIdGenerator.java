package jack.idgen;

import java.util.concurrent.atomic.AtomicLong;

/**
 * An identifier generator which runs sequentially starting at a given number.<p>
 * </p>
 * The implementation is thread-safe and can be called concurrently.
 *
 * @author Guy Raz Nir
 * @since 2024/09/17
 */
public class SequencialLongIdGenerator implements IdGenerator<Long> {

    /**
     * Internal counter.
     */
    private final AtomicLong counter = new AtomicLong();

    /**
     * Class constructor.
     */
    public SequencialLongIdGenerator() {
    }

    /**
     * Class constructor.
     *
     * @param initialValue Initial value to start with.
     */
    public SequencialLongIdGenerator(long initialValue) {
        counter.set(initialValue);
    }

    /**
     * @return Next integer identifier.
     */
    @Override
    public Long generate() {
        return counter.getAndIncrement();
    }
}
