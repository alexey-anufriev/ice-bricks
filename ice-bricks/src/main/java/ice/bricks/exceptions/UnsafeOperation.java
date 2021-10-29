package ice.bricks.exceptions;

/**
 * Represents an operation that may throw checked {@link Throwable}.
 */
@FunctionalInterface
public interface UnsafeOperation {

    void execute() throws Throwable;

}
