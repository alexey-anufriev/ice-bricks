package ice.bricks.exceptions.tests;

import ice.bricks.exceptions.ExceptionUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ExceptionUtilsTest {

    @Test
    void shouldWrapCheckedException() {
        Assertions.assertThatExceptionOfType(IllegalStateException.class).isThrownBy(
                () -> ExceptionUtils.runSafe(() -> Class.forName("MyUnknownClass")));
    }

}
