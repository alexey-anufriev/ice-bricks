package ice.bricks.io;

import java.io.IOException;

/**
 * Represents IO operation that returns a value and throws checked IOException.
 */
@FunctionalInterface
public interface IoUnsafeValueOperation<T> {

    T execute() throws IOException;

}
