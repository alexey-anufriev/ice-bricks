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
     * Performs a method call that throws checked {@link IOException}
     * replacing it with runtime {@link UncheckedIOException}.
     * Useful to write IO-related code without an obligation to catch / handle checked {@link IOException}.
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
     * @param unsafeOperation ({@link IoUnsafeOperation}) operation that throws checked {@link IOException}
     */
    public static void runSafe(IoUnsafeOperation unsafeOperation) {
        try {
            unsafeOperation.execute();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Performs a method call that returns a result and throws checked {@link IOException}
     * replacing it with runtime {@link UncheckedIOException}.
     * Useful to write IO-related code without an obligation to catch / handle checked {@link IOException}.
     *
     * Instead of:
     * <pre>
     *     try {
     *         return Paths.get("/some/dummy/path/to/file");
     *     } catch (IOException e) {
     *         throw new UncheckedIOException(e);
     *     }
     * </pre>
     *
     * the call may look like this:
     * <pre>
     *     IoUtils.runSafe(() -&gt; Paths.get("/some/dummy/path/to/file"))
     * </pre>
     *
     * @param unsafeValueOperation ({@link IoUnsafeValueOperation}) operation that throws checked {@link IOException}
     */
    public static <T> T runSafe(IoUnsafeValueOperation<T> unsafeValueOperation) {
        try {
            return unsafeValueOperation.execute();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

}
