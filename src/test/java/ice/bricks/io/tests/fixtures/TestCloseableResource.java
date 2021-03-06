package ice.bricks.io.tests.fixtures;

import lombok.Getter;

import java.io.Closeable;

@Getter
public class TestCloseableResource implements Closeable {

    private boolean closed = false;

    @Override
    public void close() {
        this.closed = true;
    }

}
