package ice.bricks.streams;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

class StreamUtilsTest {

    @Test
    void shouldCollectStream() {
        String[] strings = new String[] {"a", "b", "c"};

        assertThat(StreamUtils.collectStream(Stream.of(strings), List.class))
                .isInstanceOf(List.class)
                .hasOnlyElementsOfType(String.class)
                .containsExactly(strings);

        assertThat(StreamUtils.collectStream(Stream.of(strings), Set.class))
                .isInstanceOf(Set.class)
                .hasOnlyElementsOfType(String.class)
                .containsExactly(strings);

        assertThat(StreamUtils.collectStream(Stream.of(strings), Queue.class))
                .isInstanceOf(Queue.class)
                .hasOnlyElementsOfType(String.class)
                .containsExactly(strings);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> StreamUtils.collectStream(Stream.of(strings), Stream.class));
    }

}
