package nz.ac.canterbury.seng302.gardenersgrove.cucumber;

import org.springframework.security.core.Authentication;

/**
 * The tri consumer is an interface, so we can use a function as a .
 * This is used in our project for sending through authentication for users
 * @param <T> Declared in runCucumberTest
 * @param <U> Declared in runCumberTest
 * @param <V> Declared in runCucumberTest
 */
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

