package ice.bricks.io;

import java.io.IOException;

/**
 * Represents IO operation that returns a value may throw checked {@link IOException}.
 */
@FunctionalInterface
public interface IoUnsafeProducer<T> {

    T execute() throws IOException;

}
