package ice.bricks.reflection;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.reflect.FieldUtils;
import org.apache.commons.lang3.reflect.MethodUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

/**
 * Contains reflection-related utility methods.
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ReflectionUtils {

    /**
     * Generates an instance of a given type using a default constructor.
     * Note: input type is not constrained with generic boundaries to give more flexibility for a caller.
     *
     * @param type desired type of a newly created object
     * @param <T> desired type of a newly created object
     * @return newly created object
     */
    public static <T> T generateNewInstance(Class<?> type) {
        try {
            //noinspection unchecked
            return (T) type.getDeclaredConstructor().newInstance();
        }
        catch (Exception e) {
            String errorMessage = String.format("Unable to generate new instance of '%s'", type.getSimpleName());
            throw new InstanceCreationException(errorMessage, e);
        }
    }

    /**
     * Generates an instance of a given type using a constructor with a certain parameters.
     * Note: input type and parameters are not constrained with generic boundaries
     * to give more flexibility for a caller.
     *
     * @param type desired type of a newly created object
     * @param parameterTypes types to be used to lookup a matching constructor
     * @param parameters parameters to be used for a constructor call
     * @param <T> desired type of a newly created object
     * @return newly created object
     */
    public static <T> T generateNewInstance(Class<?> type, Class<?>[] parameterTypes, Object[] parameters) {
        try {
            //noinspection unchecked
            return (T) type.getDeclaredConstructor(parameterTypes).newInstance(parameters);
        }
        catch (Exception e) {
            String errorMessage = String.format("Unable to generate new instance of '%s'", type.getSimpleName());
            throw new InstanceCreationException(errorMessage, e);
        }
    }

    /**
     * Generates a collection instance of a given type using predefined implementations if an interface was supplied
     * or a default constructor.
     *
     * @param collectionType desired type of a newly created collection
     * @return newly created collection
     */
    public static Collection<Object> generateNewCollectionInstance(Class<?> collectionType) {
        if (collectionType.isInterface()) {
            if (List.class.isAssignableFrom(collectionType)) {
                return new ArrayList<>();
            }
            else if (Set.class.isAssignableFrom(collectionType)) {
                return new HashSet<>();
            }
            else if (Queue.class.isAssignableFrom(collectionType)) {
                return new LinkedList<>();
            }
        }

        return generateNewInstance(collectionType);
    }

    /**
     * Reads field value of a given object.
     *
     * @param object object to be used to read value from
     * @param fieldName field name to be used to read value from
     * @param <T> desired type of the field value
     * @return object field value
     */
    public static <T> T readField(Object object, String fieldName) {
        try {
            //noinspection unchecked
            return (T) FieldUtils.readField(object, fieldName, true);
        }
        catch (Exception e) {
            String errorMessage = String.format(
                    "Unable to read field %s#%s", object.getClass().getSimpleName(), fieldName);
            throw new FieldAccessException(errorMessage, e);
        }
    }

    /**
     * Writes field of a given object with a given value.
     *
     * @param object object to be used to write value to
     * @param fieldName field name to be used to write value to
     * @param value value of the filed
     */
    public static void writeField(Object object, String fieldName, Object value) {
        try {
            FieldUtils.writeField(object, fieldName, value, true);
        }
        catch (Exception e) {
            String errorMessage = String.format(
                    "Unable to write field %s#%s", object.getClass().getSimpleName(), fieldName);
            throw new FieldAccessException(errorMessage, e);
        }
    }

    /**
     * Calls the method of a given object.
     *
     * @param object object to be used to call method on
     * @param methodName method name to be used to do a call
     * @param <T> desired type of the method call result
     * @return method call result
     */
    public static <T> T invokeMethod(Object object, String methodName) {
        try {
            //noinspection unchecked
            return (T) MethodUtils.invokeMethod(object, methodName);
        }
        catch (Exception e) {
            String errorMessage = String.format("Unable to call %s#%s", object.getClass().getSimpleName(), methodName);
            throw new MethodCallException(errorMessage, e);
        }
    }

    /**
     * Calls the method of a given object with a certain parameters.
     *
     * @param object object to be used to call method on
     * @param methodName method name to be used to do a call
     * @param parameterTypes types to be used to lookup a matching method
     * @param arguments parameters to be used for a method call
     * @param <T> desired type of the method call result
     * @return method call result
     */
    public static <T> T invokeMethod(Object object, String methodName, Class<?>[] parameterTypes, Object[] arguments) {
        try {
            //noinspection unchecked
            return (T) MethodUtils.invokeMethod(object, methodName, arguments, parameterTypes);
        }
        catch (Exception e) {
            String errorMessage = String.format("Unable to call %s#%s", object.getClass().getSimpleName(), methodName);
            throw new MethodCallException(errorMessage, e);
        }
    }

}
