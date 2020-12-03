package ice.bricks.lang.model;

import com.sun.tools.javac.code.Type;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;

/**
 * Contains Java language model related utility methods.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class LanguageModelUtils {

    /**
     * Checks if the type is boolean.
     *
     * @param typeUtils instance of {@link Types}
     * @param type type to be checked
     * @return true if a given type is boolean, otherwise false
     */
    public static boolean isBooleanType(Types typeUtils, Type type) {
        String typeName = getBoxedTypeName(typeUtils, type);
        return Boolean.class.getCanonicalName().equals(typeName);
    }

    /**
     * Returns canonical name of the type. If the type is primitive it is boxed first.
     *
     * @param typeUtils instance of {@link Types}
     * @param type type to be boxed
     * @return name of the boxed type
     */
    public static String getBoxedTypeName(Types typeUtils, TypeMirror type) {
        String typeName = type.toString();

        if (type.getKind().isPrimitive()) {
            typeName = typeUtils.boxedClass((PrimitiveType) type).toString();
        }

        return typeName;
    }

}
