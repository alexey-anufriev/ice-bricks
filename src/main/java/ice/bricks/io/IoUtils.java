package ice.bricks.io;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.UncheckedIOException;

/**
 * Contains IO-related utility methods.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class IoUtils {

    /**
     * Performs a call to code that throws checked IOExceptions replacing it with runtime UncheckedIOException.
     * Useful to write IO-related code without an obligation to catch / handle checked IOExceptions.
     *
     * Instead of:
     * <pre>
     *     try {
     *         Files.delete(Paths.get("/some/dummy/path/to/delete"));
     *     } catch (IOException e) {
     *         throw new UncheckedIOException(e);
     *     }
     * </pre>
     *
     * the call may look like this:
     * <pre>
     *     IoUtils.runSafe(() -&gt; Files.delete(Paths.get("/some/dummy/path/to/delete")))
     * </pre>
     *
     * @param unsafeOperation operation that throws checked IOException {@link IoUnsafeOperation}
     */
    public static void runSafe(IoUnsafeOperation unsafeOperation) {
        try {
            unsafeOperation.execute();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
