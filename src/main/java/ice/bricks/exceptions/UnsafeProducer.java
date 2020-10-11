package ice.bricks.exceptions;

/**
 * Represents an operation that returns a value and may throw checked {@link Throwable}.
 */
@FunctionalInterface
public interface UnsafeProducer<T> {

    T execute() throws Throwable;

}
