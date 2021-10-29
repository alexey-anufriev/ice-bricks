package ice.bricks.reflection;

/**
 * Exception that is thrown when class or object method cannot be called
 */
public class MethodCallException extends RuntimeException {

    public MethodCallException(String message, Throwable cause) {
        super(message, cause);
    }

}
