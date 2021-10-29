package ice.bricks.io;

import java.io.IOException;

/**
 * Represents IO operation that consumes a value and may throw checked {@link IOException}.
 */
@FunctionalInterface
public interface IoUnsafeConsumer<T> {

    void execute(T argument) throws IOException;

}
