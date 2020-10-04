package ice.bricks.exceptions.tests;

import ice.bricks.exceptions.ExceptionUtils;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class ExceptionUtilsTest {

    @Test
    void shouldWrapCheckedException() {
        assertThatExceptionOfType(IllegalStateException.class).isThrownBy(
                () -> ExceptionUtils.runSafe(() -> Class.forName("MyUnknownClass")));
    }

}
