package ice.bricks.io.tests;

import ice.bricks.io.IoUtils;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
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

    @Test
    void shouldCloseResourceOnSuccess() {
        TestCloseableResource testCloseableResource = new TestCloseableResource();
        IoUtils.tryAndClose(() -> testCloseableResource, testResource -> {});
        Assertions.assertThat(testCloseableResource.isClosed()).isTrue();
    }

    @Test
    void shouldCloseResourceOnFailure() {
        TestCloseableResource testCloseableResource = new TestCloseableResource();
        Assertions.assertThatExceptionOfType(UncheckedIOException.class).isThrownBy(() ->
                IoUtils.tryAndClose(() -> testCloseableResource, testResource -> {
                    throw new IOException("test resource processing error");
                })
        );
        Assertions.assertThat(testCloseableResource.isClosed()).isTrue();
    }

}
