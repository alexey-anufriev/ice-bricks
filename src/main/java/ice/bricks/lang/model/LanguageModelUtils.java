package ice.bricks.lang.model;

import com.sun.tools.javac.code.Type;
import ice.bricks.meta.ClassUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Nullable;

import javax.lang.model.element.Element;
import javax.lang.model.type.PrimitiveType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

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
     * Checks if the type is void.
     *
     * @param type type to be checked
     * @return true if a given type is boolean, otherwise false
     */
    public static boolean isVoidType(Type type) {
        return type.getKind() == TypeKind.VOID;
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

    /**
     * Returns detailed type definition on the input {@link Element}
     * or {@code null} if the input not a defined type.
     *
     * @param typeElement instance of {@link Types}
     * @return instance of ({@link TypeDefinition}) with the information parsed from input or {@code null}
     */
    @Nullable
    public static TypeDefinition getTypeDefinition(Element typeElement) {
        TypeMirror typeMirror = typeElement.asType();

        if (typeMirror.getKind() == TypeKind.DECLARED) {
            String rawType = typeMirror.toString();

            if (rawType.contains("<")) {
                String rawTypeName = rawType.substring(0, rawType.indexOf("<"));
                Class<?> type = ClassUtils.getClassByName(rawTypeName);

                Type.ClassType classType = (Type.ClassType) typeMirror;
                List<Class<?>> genericTypes = classType.getTypeArguments().stream()
                        .map(typeArgument -> ClassUtils.getClassByName(typeArgument.toString()))
                        .collect(Collectors.toList());

                return new TypeDefinition(type, genericTypes);
            }

            return new TypeDefinition(ClassUtils.getClassByName(rawType), Collections.emptyList());
        }

        return null;
    }

    /**
     * Represents type definition containing the type itself with specified generics.
     */
    @Getter
    public static class TypeDefinition {

        private final Class<?> type;
        private final List<Class<?>> generics;

        private TypeDefinition(Class<?> type, List<Class<?>> generics) {
            if (type == null) {
                throw new IllegalArgumentException("Type is missing");
            }

            this.type = type;
            this.generics = generics;
        }

    }

}
