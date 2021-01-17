package ice.bricks.lang.model.tests;

import com.sun.tools.javac.code.Type;
import ice.bricks.lang.model.LanguageModelUtils;
import ice.bricks.lang.model.LanguageModelUtils.TypeDefinition;
import lombok.Builder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LanguageModelUtilsTest {

    @ParameterizedTest
    @MethodSource("testData")
    void shouldReturnTypeDefinition(TestData testData) {
        Type type = mockedType(testData);
        mockGenerics(type, testData);

        Element typeElement = mock(Element.class);
        when(typeElement.asType()).thenReturn(type);

        TypeDefinition definition = LanguageModelUtils.getTypeDefinition(mockedTypeUtils(), typeElement);
        assertThat(definition).isNotNull();
        assertThat(definition.getTypeName()).isEqualTo(testData.expectedType);
        assertThat(definition.getGenerics()).containsExactlyElementsOf(testData.expectedGenerics);
        assertThat(definition.isArray()).isEqualTo(testData.expectedArray);
    }

    private static Stream<Arguments> testData() {
        return Stream.of(
                Arguments.of(
                        TestData.builder()
                                .typeKind(TypeKind.INT)
                                .rawType("int")
                                .expectedType(Integer.class.getCanonicalName())
                                .expectedArray(false)
                                .build()),

                Arguments.of(
                        TestData.builder()
                                .typeKind(TypeKind.ARRAY)
                                .rawType("int[]")
                                .arrayComponentKind(TypeKind.INT)
                                .expectedType(Integer.class.getCanonicalName())
                                .expectedArray(true)
                                .build()),

                Arguments.of(
                        TestData.builder()
                                .typeKind(TypeKind.DECLARED)
                                .rawType("java.lang.Integer")
                                .expectedType(Integer.class.getCanonicalName())
                                .expectedArray(false)
                                .build()),

                Arguments.of(
                        TestData.builder()
                                .typeKind(TypeKind.ARRAY)
                                .rawType("java.lang.Integer[]")
                                .arrayComponentKind(TypeKind.DECLARED)
                                .expectedType(Integer.class.getCanonicalName())
                                .expectedArray(true)
                                .build()),

                Arguments.of(
                        TestData.builder()
                                .typeKind(TypeKind.DECLARED)
                                .rawType("java.util.List")
                                .expectedType(List.class.getCanonicalName())
                                .expectedArray(false)
                                .build()),

                Arguments.of(
                        TestData.builder()
                                .typeKind(TypeKind.DECLARED)
                                .rawType("java.util.List<java.lang.String>")
                                .rawGenerics(Collections.singletonList("java.lang.String"))
                                .expectedType(List.class.getCanonicalName())
                                .expectedGenerics(Collections.singletonList(String.class.getCanonicalName()))
                                .expectedArray(false)
                                .build()),

                Arguments.of(
                        TestData.builder()
                                .typeKind(TypeKind.ARRAY)
                                .rawType("java.util.List<java.lang.String>[]")
                                .arrayComponentKind(TypeKind.DECLARED)
                                .rawGenerics(Collections.singletonList("java.lang.String"))
                                .expectedType(List.class.getCanonicalName())
                                .expectedGenerics(Collections.singletonList(String.class.getCanonicalName()))
                                .expectedArray(true)
                                .build()),

                Arguments.of(
                        TestData.builder()
                                .typeKind(TypeKind.DECLARED)
                                .rawType("java.util.Map<java.lang.String, java.lang.Integer>")
                                .rawGenerics(Arrays.asList("java.lang.String", "java.lang.Integer"))
                                .expectedType(Map.class.getCanonicalName())
                                .expectedGenerics(Arrays.asList(String.class.getCanonicalName(), Integer.class.getCanonicalName()))
                                .expectedArray(false)
                                .build())
        );
    }

    private Types mockedTypeUtils() {
        // int -> java.lang.Integer conversion

        TypeMirror intMirror = mock(TypeMirror.class);
        when(intMirror.getKind()).thenReturn(TypeKind.DECLARED);
        when(intMirror.toString()).thenReturn("java.lang.Integer");

        TypeElement intElement = mock(TypeElement.class);
        when(intElement.asType()).thenReturn(intMirror);

        Types typeUtils = mock(Types.class);
        when(typeUtils.boxedClass(argThat(primitiveType -> primitiveType.getKind() == TypeKind.INT))).thenReturn(intElement);

        return typeUtils;
    }

    private Type mockedType(TestData testData) {
        Type typeMirror = null;

        if (testData.typeKind == TypeKind.DECLARED) {
            typeMirror = mock(Type.ClassType.class);
        }
        else if (testData.typeKind == TypeKind.ARRAY) {
            typeMirror = mock(Type.ArrayType.class);

            Type arrayComponentType = testData.arrayComponentKind.isPrimitive()
                    ? mock(Type.JCPrimitiveType.class)
                    : mock(Type.ClassType.class);

            when(arrayComponentType.getKind()).thenReturn(testData.arrayComponentKind);
            when(arrayComponentType.isPrimitive()).thenReturn(testData.arrayComponentKind.isPrimitive());

            when(((Type.ArrayType) typeMirror).getComponentType()).thenReturn(arrayComponentType);
        }
        else if (testData.typeKind == TypeKind.INT) {
            typeMirror = mock(Type.JCPrimitiveType.class);
        }

        when(typeMirror.getKind()).thenReturn(testData.typeKind);
        when(typeMirror.toString()).thenReturn(testData.rawType);

        return typeMirror;
    }

    private void mockGenerics(Type typeMirror, TestData testData) {
        List<Type> mockedTypeArguments = testData.rawGenerics.stream()
                .map(typeArgument -> {
                    Type type = mock(Type.class);
                    when(type.toString()).thenReturn(typeArgument);
                    return type;
                })
                .collect(Collectors.toList());

        if (testData.typeKind == TypeKind.DECLARED) {
            when(typeMirror.getTypeArguments()).thenReturn(com.sun.tools.javac.util.List.from(mockedTypeArguments));
        }
        else if (testData.typeKind == TypeKind.ARRAY) {
            when(typeMirror.allparams()).thenReturn(com.sun.tools.javac.util.List.from(mockedTypeArguments));
        }
    }

    @Builder
    private static class TestData {
        private final TypeKind typeKind;
        private final TypeKind arrayComponentKind;
        private final String rawType;

        @Builder.Default
        private final List<String> rawGenerics = Collections.emptyList();

        private final String expectedType;

        @Builder.Default
        private final List<String> expectedGenerics = Collections.emptyList();
        private final boolean expectedArray;
    }

}
