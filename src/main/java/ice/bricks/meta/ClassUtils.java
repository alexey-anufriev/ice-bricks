package ice.bricks.meta;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Nullable;

/**
 * Contains general class-related utility methods.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ClassUtils {

    /**
     * Returns class package without a class name out of canonical class name,
     * or {@code null} if class has no package defined.
     *
     * @param canonicalClassName canonical class name
     * @return class package
     */
    @Nullable
    public static String getClassPackage(String canonicalClassName) {
        if (canonicalClassName.indexOf('.') >= 0) {
            return canonicalClassName.substring(0, canonicalClassName.lastIndexOf('.'));
        }

        return null;
    }

    /**
     * Returns class name out of canonical class name.
     *
     * @param canonicalClassName canonical class name
     * @return class name
     */
    public static String getClassName(String canonicalClassName) {
        if (canonicalClassName.indexOf('.') >= 0) {
            return canonicalClassName.substring(canonicalClassName.lastIndexOf('.') + 1);
        }

        return canonicalClassName;
    }

    /**
     * Returns class name its string name, or {@code null} if class cannot be loaded.
     *
     * @param className canonical class name
     * @return class looked up by name
     */
    @Nullable
    public static Class<?> getClassByName(String className) {
        try {
            return Class.forName(className);
        }
        catch (Exception ignored) {
            return null;
        }
    }

}
