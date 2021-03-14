package ice.bricks.lang.model.tests;

import ice.bricks.lang.model.LanguageModelUtils.TypeDetails;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

class TypeDetailsTests {

    @Test
    void shouldGenerateSimpleStringRepresentation() {
        TypeDetails stringTypeDetails = TypeDetails.builder()
                .typeName(String.class.getCanonicalName())
                .build();

        assertThat(stringTypeDetails).hasToString("java.lang.String");
    }

    @Test
    void shouldGenerateComplexStringRepresentation() {
        TypeDetails keyTypeDetails = TypeDetails.builder()
                .typeName(String.class.getCanonicalName())
                .build();

        TypeDetails intTypeDetails = TypeDetails.builder()
                .typeName(Integer.class.getCanonicalName())
                .build();

        TypeDetails valueTypeDetails = TypeDetails.builder()
                .typeName(List.class.getCanonicalName())
                .generics(Collections.singletonList(intTypeDetails))
                .build();

        TypeDetails complexTypeDetails = TypeDetails.builder()
                .typeName(Map.class.getCanonicalName())
                .generics(Arrays.asList(keyTypeDetails, valueTypeDetails))
                .build();

        assertThat(complexTypeDetails).hasToString("java.util.Map<java.lang.String, java.util.List<java.lang.Integer>>");
    }
}
