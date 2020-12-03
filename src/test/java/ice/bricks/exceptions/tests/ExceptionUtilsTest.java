package ice.bricks.exceptions.tests;

import ice.bricks.exceptions.ExceptionUtils;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
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

    @Test
    void shouldChainExceptions() {
        Exception original = new Exception("Original exception");
        Exception suppressor = new Exception("Suppressor exception");

        assertThat(ExceptionUtils.chainExceptions(null, null)).isNull();
        assertThat(ExceptionUtils.chainExceptions(original, null)).isEqualTo(original);
        assertThat(ExceptionUtils.chainExceptions(null, suppressor)).isEqualTo(suppressor);

        assertThat(ExceptionUtils.chainExceptions(original, suppressor))
                .hasMessage("Suppressor exception")
                .hasStackTraceContaining("Original exception");
    }

}
