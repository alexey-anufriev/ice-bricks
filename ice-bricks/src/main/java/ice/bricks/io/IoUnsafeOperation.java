package ice.bricks.io;

import java.io.IOException;

/**
 * Represents IO operation that may throw checked {@link IOException}.
 */
@FunctionalInterface
public interface IoUnsafeOperation {

    void execute() throws IOException;

}
