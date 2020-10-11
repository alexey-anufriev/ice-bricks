package ice.bricks.exceptions.tests;

import ice.bricks.exceptions.ExceptionUtils;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ExceptionUtilsTest {

    @Test
    void shouldWrapCheckedExceptionForVoidCall() {
        assertThatExceptionOfType(IllegalStateException.class).isThrownBy(
                () -> ExceptionUtils.runSafe(() -> Class.forName("MyUnknownClass")));
    }

    @Test
    void shouldWrapCheckedExceptionForResultCall() {
        assertThatExceptionOfType(IllegalStateException.class).isThrownBy(() -> {
            Class<?> clazz = ExceptionUtils.runSafe(() -> Class.forName("MyUnknownClass"));
        });
    }

}
