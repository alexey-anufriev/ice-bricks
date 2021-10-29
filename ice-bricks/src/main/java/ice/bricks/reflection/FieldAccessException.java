package ice.bricks.reflection;

/**
 * Exception that is thrown when class or object field cannot be accessed
 */
public class FieldAccessException extends RuntimeException {

    public FieldAccessException(String message, Throwable cause) {
        super(message, cause);
    }

}
