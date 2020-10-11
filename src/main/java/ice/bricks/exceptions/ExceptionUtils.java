package ice.bricks.exceptions;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * Contains exceptions-related utility methods.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ExceptionUtils {

    /**
     * Performs a method call that may throw checked exception, replacing it with runtime {@link IllegalStateException}.
     * Useful to write code without an obligation to catch / handle checked exceptions.
     *
     * Instead of:
     * <pre>
     *     try {
     *         MethodUtils.invokeMethod(object, methodName);
     *     } catch (Exception e) {
     *         throw new IllegalStateException(e);
     *     }
     * </pre>
     *
     * the call may look like this:
     * <pre>
     *     ExceptionUtils.runSafe(() -&gt; MethodUtils.invokeMethod(object, methodName));
     * </pre>
     *
     * @param unsafeOperation ({@link UnsafeOperation}) operation that throws checked exception
     */
    public static void runSafe(UnsafeOperation unsafeOperation) {
        try {
            unsafeOperation.execute();
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }

    /**
     * Performs a method call that returns a result and may throw checked exception,
     * replacing it with runtime {@link IllegalStateException}.
     * Useful to write code without an obligation to catch / handle checked exceptions.
     *
     * Instead of:
     * <pre>
     *     boolean check;
     *     try {
     *         check = checkFuture.get(); // Future&lt;Boolean&gt;
     *     }
     *     catch (Exception e) {
     *         throw new IllegalStateException(e);
     *     }
     * </pre>
     *
     * the call may look like this:
     * <pre>
     *     boolean check = ExceptionUtils.runSafe(() -&gt; checkFuture.get());
     * </pre>
     *
     * @param unsafeProducer ({@link UnsafeProducer}) operation that throws checked exception
     * @param <T> specific type of the result
     * @return result of the operation
     */
    public static <T> T runSafe(UnsafeProducer<T> unsafeProducer) {
        try {
            return unsafeProducer.execute();
        } catch (Throwable e) {
            throw new IllegalStateException(e);
        }
    }

}
