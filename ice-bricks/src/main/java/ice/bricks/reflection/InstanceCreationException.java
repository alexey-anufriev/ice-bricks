package ice.bricks.reflection;

/**
 * Exception that is thrown when object instance of a given type cannot be created
 */
public class InstanceCreationException extends RuntimeException {

    public InstanceCreationException(String message, Throwable cause) {
        super(message, cause);
    }

}
