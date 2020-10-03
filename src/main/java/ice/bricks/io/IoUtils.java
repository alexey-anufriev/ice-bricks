package ice.bricks.io;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.Closeable;
import java.io.IOException;
import java.io.UncheckedIOException;

/**
 * Contains IO-related utility methods.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class IoUtils {

    /**
     * Performs a method call that may throw checked {@link IOException}
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
     *     IoUtils.runSafe(() -&gt; Files.delete(Paths.get("/some/dummy/path/to/delete")));
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
     * Performs a method call that returns a result and may throw checked {@link IOException}
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
     *     IoUtils.runSafe(() -&gt; Paths.get("/some/dummy/path/to/file"));
     * </pre>
     *
     * @param unsafeValueOperation ({@link IoUnsafeProducer}) operation that throws checked {@link IOException}
     * @param <T> specific type of the result
     * @return result of the operation
     */
    public static <T> T runSafe(IoUnsafeProducer<T> unsafeValueOperation) {
        try {
            return unsafeValueOperation.execute();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /**
     * Obtains a resource, supplies it to the consumer and then safely closes it.
     * Useful to reduce the code while working with
     *
     * Instead of:
     * <pre>
     *     Writer writer = null;
     *     try {
     *         writer = getWriter();
     *         writeData(writer);
     *     } catch (IOException e) {
     *         throw new UncheckedIOException(e);
     *     } finally {
     *         if (writer != null) {
     *             try {
     *                 writer.close();
     *             } catch (IOException e) {
     *                 throw new UncheckedIOException(e);
     *             }
     *         }
     *     }
     * </pre>
     *
     * the code may look like this:
     * <pre>
     *     IoUtils.tryAndClose(this::getWriter, this::writeData);
     * </pre>
     *
     * @param resourceSupplier supplier of the resource
     * @param resourceConsumer consumer of the resource
     * @param <T> specific type of the resource
     */
    public static <T extends Closeable> void tryAndClose(IoUnsafeProducer<T> resourceSupplier,
                                                         IoUnsafeConsumer<T> resourceConsumer) {
        T resource = null;

        try {
            resource = resourceSupplier.execute();
            resourceConsumer.execute(resource);
        }
        catch (IOException e) {
            throw new UncheckedIOException(e);
        }
        finally {
            if (resource != null) {
                runSafe(resource::close);
            }
        }
    }

}
