package ice.bricks.lang.model;

import com.sun.tools.javac.code.Type;
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
     * Returns boxed type of the primitive type.
     *
     * @param typeUtils instance of {@link Types}
     * @param type type to be boxed
     * @return boxed type
     */
    public static TypeMirror getBoxedType(Types typeUtils, TypeMirror type) {
        if (type.getKind().isPrimitive()) {
            return typeUtils.boxedClass((PrimitiveType) type).asType();
        }

        return type;
    }

    /**
     * Returns canonical name of the type. If the type is primitive it is boxed first.
     *
     * @param typeUtils instance of {@link Types}
     * @param type type to be boxed
     * @return name of the boxed type
     */
    public static String getBoxedTypeName(Types typeUtils, TypeMirror type) {
        return getBoxedType(typeUtils, type).toString();
    }

    /**
     * Returns detailed type definition on the input {@link Element}
     * or {@code null} if the input not a defined type.
     *
     * @param typeUtils instance of {@link Types}
     * @param typeElement instance of {@link Element}
     * @return instance of ({@link TypeDefinition}) with the information parsed from input or {@code null}
     */
    @Nullable
    public static TypeDefinition getTypeDefinition(Types typeUtils, Element typeElement) {
        TypeMirror typeMirror = typeElement.asType();

        boolean declaredType = typeMirror.getKind() == TypeKind.DECLARED;

        boolean isArray = typeMirror.getKind() == TypeKind.ARRAY;
        if (isArray) {
            // check if array of primitives supplied
            Type arrayComponentType = ((Type.ArrayType) typeMirror).getComponentType();
            if (arrayComponentType.isPrimitive()) {
                typeMirror = getBoxedType(typeUtils, arrayComponentType);
            }
        }

        boolean isPrimitive = typeMirror.getKind().isPrimitive();
        if (isPrimitive) {
            typeMirror = getBoxedType(typeUtils, typeMirror);
        }

        if (declaredType || isPrimitive || isArray) {
            String rawType = typeMirror.toString();

            if (isArray) {
                rawType = rawType.replace("[]", "");
            }

            if (rawType.contains("<")) {
                String rawTypeName = rawType.substring(0, rawType.indexOf("<"));

                com.sun.tools.javac.util.List<Type> genericParams = null;

                if (declaredType) {
                    Type.ClassType classType = (Type.ClassType) typeMirror;
                    genericParams = classType.getTypeArguments();
                }
                else if (isArray) {
                    Type.ArrayType arrayType = (Type.ArrayType) typeMirror;
                    genericParams = arrayType.allparams();
                }

                List<String> genericTypes = genericParams.stream()
                        .map(Type::toString)
                        .collect(Collectors.toList());

                return new TypeDefinition(rawTypeName, genericTypes, isArray);
            }

            return new TypeDefinition(rawType, Collections.emptyList(), isArray);
        }

        return null;
    }

    /**
     * Represents type definition containing the type name with names of specified generics.
     */
    @Getter
    public static class TypeDefinition {

        private final String typeName;
        private final List<String> generics;
        private final boolean isArray;

        private TypeDefinition(String typeName, List<String> generics, boolean isArray) {
            if (typeName == null) {
                throw new IllegalArgumentException("Type is missing");
            }

            this.typeName = typeName;
            this.generics = generics;
            this.isArray = isArray;
        }

    }

}
