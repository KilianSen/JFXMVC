package de.kiliansen.lib.JFXMVC;

import java.util.concurrent.atomic.AtomicReference;

/**
 * Model is a simple thread-safe model class that holds a value of type T.
 * It provides methods to get and set the value in a synchronized manner.
 *
 * @param <T> the type of the value held by this model
 */
public class Model<T> {
    /**
     * The value held by this model.
     * This is an AtomicReference to allow thread-safe access and modification.
     */
    private final AtomicReference<T> value;

    /**
     * Constructs a Model with an initial value.
     *
     * @param initialValue the initial value to set
     */
    public Model(T initialValue) {
        this.value = new AtomicReference<>(initialValue);
    }

    /**
     * Gets the current value of this model.
     */
    public synchronized T get() {
        return value.get();
    }

    /**
     * Sets a new value for this model.
     *
     * @param newValue the new value to set
     */
    public synchronized void set(T newValue) {
        value.set(newValue);
    }
}
