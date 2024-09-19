package jack.objects;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Represents a state of object (a collection of values that includes all relevant fields).<p>
 * </p>
 * An implementation may choose to provide only a subset of the object's fields' states.
 *
 * @author Guy Raz Nir
 * @since 2021/09/30
 */
public class ObjectState implements Serializable {

    /**
     * Collection of values representing state.
     */
    private final Object[] state;

    /**
     * Class constructor.
     *
     * @param state State.
     */
    public ObjectState(Object... state) {
        this.state = state;
    }

    @Override
    public boolean equals(Object o) {
        return (o == this) || o instanceof ObjectState os && Arrays.equals(this.state, os.state);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(state);
    }
}
