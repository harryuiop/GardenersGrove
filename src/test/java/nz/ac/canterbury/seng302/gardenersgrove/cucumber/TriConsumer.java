package nz.ac.canterbury.seng302.gardenersgrove.cucumber;

import org.springframework.security.core.Authentication;

@FunctionalInterface
public interface TriConsumer<T, U, V> {
    Authentication accept(T t, U u, V v);

    // Default method to allow chaining
    default nz.ac.canterbury.seng302.gardenersgrove.cucumber.TriConsumer<T, U, V> andThen(nz.ac.canterbury.seng302.gardenersgrove.cucumber.TriConsumer<? super T, ? super U, ? super V> after) {
        if (after == null) {
            throw new NullPointerException();
        }
        return (t, u, v) -> {
            accept(t, u, v);
            return after.accept(t, u, v);
        };
    }
}

