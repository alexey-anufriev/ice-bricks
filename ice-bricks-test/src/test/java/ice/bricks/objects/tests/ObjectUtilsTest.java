package ice.bricks.objects.tests;

import ice.bricks.objects.ObjectUtils;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ObjectUtilsTest {

    @Test
    void shouldReturnNonDefaultValue() {
        Integer nonDefaultValue = ObjectUtils.defaultIf(10, 15, value -> value < 5);
        assertThat(nonDefaultValue).isEqualTo(10);
    }

    @Test
    void shouldReturnDefaultValue() {
        Integer defaultValue = ObjectUtils.defaultIf(10, 15, value -> value > 5);
        assertThat(defaultValue).isEqualTo(15);
    }

}
