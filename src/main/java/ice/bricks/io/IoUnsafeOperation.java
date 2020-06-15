package ice.bricks.io;

import java.io.IOException;

/**
 * Represents IO operation that throws checked IOException.
 */
@FunctionalInterface
public interface IoUnsafeOperation {

    void execute() throws IOException;

}
