package ice.bricks.io.tests;

import ice.bricks.io.IoUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;

class IoUtilsTest {

    @Test
    void shouldWrapCheckedIoException() {
        Assertions.assertThatExceptionOfType(UncheckedIOException.class).isThrownBy(
                () -> IoUtils.runSafe(() -> Files.delete(Paths.get("/some/dummy/path/to/delete"))));
    }

    @Test
    void shouldReturnResultWithoutExceptionCheck() {
        Assertions.assertThat(IoUtils.runSafe(() -> Paths.get("."))).isNotNull();
    }

}
