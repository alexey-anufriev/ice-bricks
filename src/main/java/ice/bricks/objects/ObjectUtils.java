package ice.bricks.objects;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.function.Predicate;

/**
 * Contains general objects-related utility methods.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ObjectUtils {

    /**
     * Returns a value if the condition is evaluated to {@code false}
     * or a default value if the condition is evaluated to {@code false}.
     *
     * Instead of:
     * <pre>
     *     Integer value = someValue;
     *     if (value &gt; 5) {
     *         value = 15;
     *     }
     * </pre>
     *
     * the code may look like this:
     * <pre>
     *     Integer value = ObjectUtils.defaultIf(someValue, 15, value -&gt; value &gt; 5);
     * </pre>
     *
     * @param value a value to be returned if the condition is evaluated to {@code false}
     * @param defaultValue a value to be returned if the condition is evaluated to {@code false}
     * @param condition condition to decide on the returned value
     * @param <T> specific type of the result
     * @return value defined by the condition evaluation
     */
    public static <T> T defaultIf(T value, T defaultValue, Predicate<T> condition) {
        return condition.test(value) ? defaultValue : value;
    }

}
