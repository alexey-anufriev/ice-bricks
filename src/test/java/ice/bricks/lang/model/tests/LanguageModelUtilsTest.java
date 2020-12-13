package ice.bricks.lang.model.tests;

import com.sun.tools.javac.code.Type;
import ice.bricks.lang.model.LanguageModelUtils;
import ice.bricks.lang.model.LanguageModelUtils.TypeDefinition;
import lombok.Builder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.lang.model.element.Element;
import javax.lang.model.type.TypeKind;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class LanguageModelUtilsTest {

    @ParameterizedTest
    @MethodSource("testData")
    void shouldReturnTypeDefinition(TestData testData) {
        Element typeElement = mock(Element.class);

        Type.ClassType typeMirror = mock(Type.ClassType.class);
        when(typeMirror.getKind()).thenReturn(testData.typeKind);
        when(typeMirror.toString()).thenReturn(testData.rawType);

        List<Type> mockedTypeArguments = testData.rawTypeArguments.stream()
                .map(typeArgument -> {
                    Type type = mock(Type.class);
                    when(type.toString()).thenReturn(typeArgument);
                    return type;
                })
                .collect(Collectors.toList());

        when(typeMirror.getTypeArguments()).thenReturn(com.sun.tools.javac.util.List.from(mockedTypeArguments));

        when(typeElement.asType()).thenReturn(typeMirror);

        TypeDefinition definition = LanguageModelUtils.getTypeDefinition(typeElement);

        if (testData.expectedResult) {
            assertThat(definition).isNotNull();
            assertThat(definition.getType()).isEqualTo(testData.expectedType);
            assertThat(definition.getGenerics()).containsExactlyElementsOf(testData.expectedGenerics);
        }
        else {
            assertThat(definition).isNull();
        }
    }

    private static Stream<Arguments> testData() {
        return Stream.of(
                Arguments.of(
                        TestData.builder()
                                .typeKind(TypeKind.INT)
                                .rawType("int")
                                .rawTypeArguments(Collections.emptyList())
                                .expectedResult(false)
                                .build()),

                Arguments.of(
                        TestData.builder()
                                .typeKind(TypeKind.DECLARED)
                                .rawType("java.lang.Integer")
                                .rawTypeArguments(Collections.emptyList())
                                .expectedResult(true)
                                .expectedType(Integer.class)
                                .expectedGenerics(Collections.emptyList())
                                .build()),

                Arguments.of(
                        TestData.builder()
                                .typeKind(TypeKind.DECLARED)
                                .rawType("java.util.List")
                                .rawTypeArguments(Collections.emptyList())
                                .expectedResult(true)
                                .expectedType(List.class)
                                .expectedGenerics(Collections.emptyList())
                                .build()),

                Arguments.of(
                        TestData.builder()
                                .typeKind(TypeKind.DECLARED)
                                .rawType("java.util.List<String>")
                                .rawTypeArguments(Collections.singletonList("java.lang.String"))
                                .expectedResult(true)
                                .expectedType(List.class)
                                .expectedGenerics(Collections.singletonList(String.class))
                                .build()),

                Arguments.of(
                        TestData.builder()
                                .typeKind(TypeKind.DECLARED)
                                .rawType("java.util.Map<String, Integer>")
                                .rawTypeArguments(Arrays.asList("java.lang.String", "java.lang.Integer"))
                                .expectedResult(true)
                                .expectedType(Map.class)
                                .expectedGenerics(Arrays.asList(String.class, Integer.class))
                                .build())
        );
    }

    @Builder
    private static class TestData {
        private final TypeKind typeKind;
        private final String rawType;
        private final List<String> rawTypeArguments;

        private final boolean expectedResult;
        private final Class<?> expectedType;
        private final List<Class<?>> expectedGenerics;
    }

}
