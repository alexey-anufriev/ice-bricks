package ice.bricks.lang.model;

import com.sun.tools.javac.code.Type;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.Nullable;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
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
     * Returns detailed type definition on the input {@link TypeMirror}
     * or {@code null} if the input not a defined type.
     *
     * @param typeUtils instance of {@link Types}
     * @param typeMirror instance of {@link TypeMirror} to be analyzed
     * @return instance of ({@link TypeDetails}) with the information parsed from input or {@code null}
     */
    @Nullable
    public static TypeDetails getTypeDetails(Types typeUtils, TypeMirror typeMirror) {
        boolean isArray = typeMirror.getKind() == TypeKind.ARRAY;

        if (isArray) {
            typeMirror = ((Type.ArrayType) typeMirror).getComponentType();
        }

        if (typeMirror.getKind() == TypeKind.WILDCARD) {
            Type.WildcardType wildcardType = (Type.WildcardType) typeMirror;

            // ? super T => Object
            if (wildcardType.isSuperBound()) {
                String typeName = Object.class.getCanonicalName();

                return TypeDetails.builder()
                        .typeName(typeName)
                        .boxedTypeName(typeName)
                        .isArray(isArray)
                        .build();
            }

            // ? extends T => T
            typeMirror = wildcardType.getExtendsBound();
        }

        boolean declaredType = typeMirror.getKind() == TypeKind.DECLARED;

        boolean isAbstract = declaredType && ((Type.ClassType) typeMirror).asElement()
                .getModifiers().contains(Modifier.ABSTRACT);

        boolean isInterface = declaredType && ((Type.ClassType) typeMirror).asElement()
                .getKind() == ElementKind.INTERFACE;

        boolean isPrimitive = typeMirror.getKind().isPrimitive();

        if (declaredType || isPrimitive || isArray) {
            String rawType = typeMirror.toString();
            String boxedType = isPrimitive
                    ? getBoxedType(typeUtils, typeMirror).toString()
                    : rawType;

            if (isArray) {
                rawType = rawType.replace("[]", "");
            }

            if (rawType.contains("<")) {
                rawType = rawType.substring(0, rawType.indexOf("<"));

                List<TypeDetails> genericTypes = ((Type) typeMirror).allparams().stream()
                        .map(type -> getTypeDetails(typeUtils, type))
                        .collect(Collectors.toList());

                return TypeDetails.builder()
                        .typeName(rawType)
                        .boxedTypeName(boxedType)
                        .isPrimitive(isPrimitive)
                        .isArray(isArray)
                        .isAbstract(isAbstract)
                        .isInterface(isInterface)
                        .generics(genericTypes)
                        .build();
            }

            List<TypeDetails> noGenerics = Collections.emptyList();

            return TypeDetails.builder()
                    .typeName(rawType)
                    .boxedTypeName(boxedType)
                    .isPrimitive(isPrimitive)
                    .isArray(isArray)
                    .isAbstract(isAbstract)
                    .isInterface(isInterface)
                    .generics(noGenerics)
                    .build();
        }

        return null;
    }

    @Getter
    @Builder
    public static class TypeDetails {

        private final String typeName;
        private final String boxedTypeName;
        private final boolean isPrimitive;
        private final boolean isArray;
        private final boolean isAbstract;
        private final boolean isInterface;

        @Builder.Default
        private final List<TypeDetails> generics = Collections.emptyList();

        @Override
        public String toString() {
            if (!this.generics.isEmpty()) {
                String typeParameters = this.generics.stream()
                        .map(TypeDetails::toString)
                        .collect(Collectors.joining(", "));

                return this.typeName + "<" + typeParameters + ">";
            }
            else {
                return this.typeName;
            }
        }

    }

}
