package ice.bricks.io.tests;

import ice.bricks.io.IoUtils;
import ice.bricks.io.tests.fixtures.TestCloseableResource;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

class IoUtilsTest {

    @Test
    void shouldWrapCheckedIoException() {
        assertThatExceptionOfType(UncheckedIOException.class).isThrownBy(
                () -> IoUtils.runSafe(() -> Files.delete(Paths.get("/some/dummy/path/to/delete"))));
    }

    @Test
    void shouldReturnResultWithoutExceptionCheck() {
        assertThat(IoUtils.runSafe(() -> Paths.get("."))).isNotNull();
    }

    @Test
    void shouldCloseResourceIfSuccess() {
        TestCloseableResource testCloseableResource = new TestCloseableResource();
        IoUtils.tryAndClose(() -> testCloseableResource, testResource -> {});
        assertThat(testCloseableResource.isClosed()).isTrue();
    }

    @Test
    void shouldCloseResourceIfFailure() {
        TestCloseableResource testCloseableResource = new TestCloseableResource();
        assertThatExceptionOfType(UncheckedIOException.class).isThrownBy(() ->
                IoUtils.tryAndClose(() -> testCloseableResource, testResource -> {
                    throw new IOException("test resource processing error");
                })
        );
        assertThat(testCloseableResource.isClosed()).isTrue();
    }

}
