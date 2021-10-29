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
     * the code may look like this:
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
     * the code may look like this:
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

    /**
     * Performs a method call that returns a result and may throw checked exception,
     * suppressing an exception with a default value.
     * Useful to write code without an obligation to catch / handle checked exceptions.
     *
     * Instead of:
     * <pre>
     *     boolean check;
     *     try {
     *         check = checkFuture.get(); // Future&lt;Boolean&gt;
     *     }
     *     catch (Exception e) {
     *         check = false;
     *     }
     * </pre>
     *
     * the code may look like this:
     * <pre>
     *     boolean check = ExceptionUtils.defaultIfException(() -&gt; checkFuture.get(), false);
     * </pre>
     *
     * @param unsafeProducer ({@link UnsafeProducer}) operation that throws checked exception
     * @param defaultValue default value to be returned in case of exception
     * @param <T> specific type of the result
     * @return result of the operation
     */
    public static <T> T defaultIfException(UnsafeProducer<T> unsafeProducer, T defaultValue) {
        try {
            return unsafeProducer.execute();
        } catch (Throwable e) {
            return defaultValue;
        }
    }

    /**
     * Combines exceptions in a chain. This allows to build a structure of nested exceptions within a root one.
     *
     * Chaining of exceptions can be done in the following way
     * <pre>
     *     Exception root = new Exception("Message");
     *     // ...
     *     root = chainExceptions(root, new Exception("Other message"));
     *     // ...
     *     root = chainExceptions(root, new Exception("Yet another message"));
     * </pre>
     *
     * Final stack trace will contain all the exceptions
     * <pre>
     *     Exception java.lang.Exception: Yet another message
     *       at ...
     *         Suppressed: java.lang.Exception: Other message
     *           at ...
     *             Suppressed: java.lang.Exception: Message
     *               at ...
     * </pre>
     *
     * @param suppressed original exception
     * @param suppressor newly occurred exception that suppresses the original one
     * @return suppressor exception with chained suppressed exception
     */
    public static Throwable chainExceptions(Throwable suppressed, Throwable suppressor) {
        if (suppressed == null && suppressor == null) {
            return null;
        }

        if (suppressed == null) {
            return suppressor;
        }

        if (suppressor == null) {
            return suppressed;
        }

        suppressor.addSuppressed(suppressed);
        return suppressor;
    }

}
